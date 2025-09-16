package com.tesis.proyect.app.application.usecases.userinterview;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.input.userinterview.SaveUserInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.AwsExternalServicePort;
import com.tesis.proyect.app.domain.ports.output.IAExternalServicePort;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.WhisperExternalServicePort;
import com.tesis.proyect.app.utils.EstadoEntrevista;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class SaveUserInterviewUseCaseImpl implements SaveUserInterviewUseCase {

    private final WhisperExternalServicePort whisperExternalServicePort;
    private final UserInterviewRepositoryPort interviewRepositoryPort;
    private final AwsExternalServicePort awsExternalServicePort;
    private final IAExternalServicePort iaExternalServicePort;

    @Override
    public Mono<UserInterview> saveUserInterview
            (Flux<FilePart> audios, FilePart fullVideo, String userId, String interviewId) {

        String originalFilename = fullVideo.filename();
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String videoKey = "entrevista/" + UUID.randomUUID() + extension;

        return awsExternalServicePort.uploadFile(videoKey, fullVideo) // sube el video
                .flatMap(success -> {
                    if (!success) {
                        return Mono.error(new RuntimeException("Error al subir el video a S3"));
                    }

                    return getAnswers(audios) // procesar audios
                            .collectList()
                            .flatMap(answers -> {
                                UserInterview userInterview = new UserInterview();
                                userInterview.setS3KeyPath(videoKey);
                                userInterview.setDate(LocalDate.now());
                                userInterview.setUserId(userId);
                                userInterview.setInterviewId(interviewId);
                                userInterview.setAnswers(answers);
                                userInterview.setScore(0);
                                userInterview.setState(EstadoEntrevista.PENDIENTE.name());

                                return interviewRepositoryPort.save(userInterview);
                            });
                });
    }

    @Override
    public Mono<String> devolverPropmt(String prompt) {
        return iaExternalServicePort.getIaServiceResponse(prompt);
    }

    private Flux<UserInterview.Answer> getAnswers(Flux<FilePart> audios) {
        return audios.flatMap(audio ->
                whisperExternalServicePort.getTextFromFile(audio) // devuelve Mono<String>
                        .map(text -> {
                            String originalName = audio.filename();
                            String filenameWithoutExt = originalName.contains(".")
                                    ? originalName.substring(0, originalName.lastIndexOf('.'))
                                    : originalName;
                            return new UserInterview.Answer(filenameWithoutExt, text);
                        })
        );
    }
}
