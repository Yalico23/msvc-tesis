package com.tesis.proyect.app.application.usecases.userinterview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
                            .flatMap(this::evaluateAnswerWithIA)
                            .collectList()
                            .flatMap(answers -> {
                                // Calcular promedio de puntaje
                                int score = answers.stream()
                                        .mapToInt(UserInterview.Answer::getPoints)
                                        .sum();

                                UserInterview userInterview = new UserInterview();
                                userInterview.setS3KeyPath(videoKey);
                                userInterview.setDate(LocalDate.now());
                                userInterview.setUserId(userId);
                                userInterview.setInterviewId(interviewId);
                                userInterview.setAnswers(answers);
                                userInterview.setScore(score);
                                userInterview.setDuration(null); // duración no implementada
                                userInterview.setState(EstadoEntrevista.PENDIENTE.name());

                                return interviewRepositoryPort.save(userInterview);
                            });
                });
    }

    private Mono<String> makePrompt(String question, String answer) {
        String prompt = """
                Eres un experto de recursos humanos que evaluará una respuesta en una entrevista.
                Muy importante al momento de calificar si hay palabras sin sentido es por un tema de audio a texto puede que por ello encuentres fallas ortográficas o palabras que no tienen sentido. Evalúa lo que se llegue a entender, si la respuesta no tiene nada que ver con la pregunta directamente calificalo con 0
                La pregunta es: "%s"
                La respuesta dada es: "%s"
                
                Devuelve SOLO un JSON válido con los campos:
                {
                  "score": <número entre 0 y 10>,
                  "justification": "<máx 2 líneas explicando el puntaje>"
                }
                """
                .formatted(question, answer);
        return Mono.just(prompt);
    }

    private String sanitizeJson(String rawResponse) {
        if (rawResponse == null) return "{}";

        String cleaned = rawResponse.replaceAll("(?s)```json", "")
                .replaceAll("(?s)```", "")
                .trim();

        if (cleaned.startsWith("json")) {
            cleaned = cleaned.substring(4).trim();
        }

        // Escape de comillas dobles no escapadas dentro de justification u otros campos
        // Básicamente: si hay una comilla dentro de un valor JSON, la escapamos
        cleaned = cleaned.replaceAll(":(\\s*)\"([^\"]*?)\"(\\s*[},])",
                        ": \"$2\"$3") // deja pasar el valor correcto
                .replaceAll("([^\\\\])\"([^\":{}]+)\"", "$1\\\"$2\\\""); // escapa las internas

        return cleaned;
    }


    private Mono<UserInterview.Answer> evaluateAnswerWithIA(UserInterview.Answer answer) {
        return makePrompt(answer.getQuestionText(), answer.getResponseText())
                .flatMap(iaExternalServicePort::getIaServiceResponse)
                .map(this::sanitizeJson) // 👈 limpiar la respuesta
                .flatMap(rawResponse -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode node = mapper.readTree(rawResponse);

                        if (node.has("score") && node.has("justification")) {
                            answer.setPoints(node.get("score").asInt());
                            answer.setDescription(node.get("justification").asText());
                            return Mono.just(answer);
                        } else {
                            return Mono.error(new RuntimeException("Respuesta IA inválida: " + rawResponse));
                        }
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error parseando respuesta IA: " + rawResponse, e));
                    }
                });
    }


    private Flux<UserInterview.Answer> getAnswers(Flux<FilePart> audios) {
        return audios.flatMap(audio ->
                whisperExternalServicePort.getTextFromFile(audio) // devuelve Mono<String>
                        .map(text -> {
                            String originalName = audio.filename();
                            String filenameWithoutExt = originalName.contains(".")
                                    ? originalName.substring(0, originalName.lastIndexOf('.'))
                                    : originalName;
                            return new UserInterview.Answer(filenameWithoutExt, text, null, null);
                        })
        );
    }
}
