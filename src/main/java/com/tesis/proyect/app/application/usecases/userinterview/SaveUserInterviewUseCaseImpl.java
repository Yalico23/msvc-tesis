package com.tesis.proyect.app.application.usecases.userinterview;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.proyect.app.domain.exceptions.UserNotFoundException;
import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.input.userinterview.SaveUserInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.*;
import com.tesis.proyect.app.utils.EstadoEntrevista;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class SaveUserInterviewUseCaseImpl implements SaveUserInterviewUseCase {

    private final WhisperExternalServicePort whisperExternalServicePort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserInterviewRepositoryPort interviewRepositoryPort;
    private final AwsExternalServicePort awsExternalServicePort;
    private final IAExternalServicePort iaExternalServicePort;

    @Override
    public Mono<UserInterview> saveUserInterview(String userId, String interviewId) {

        return userRepositoryPort.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with id: " + userId)))
                .flatMap(user -> {
                    user.setInterviewAsignedId(interviewId);
                    return userRepositoryPort.save(user);
                })
                .flatMap(savedUser -> {
                    UserInterview userInterview = new UserInterview();
                    userInterview.setDate(LocalDate.now());
                    userInterview.setState(EstadoEntrevista.PENDIENTE.name());
                    userInterview.setScore(0);
                    userInterview.setS3KeyPath("");
                    userInterview.setInterviewId(interviewId);
                    userInterview.setUserId(savedUser.getId());
                    userInterview.setDuration(0);
                    userInterview.setAnswers(null);
                    return interviewRepositoryPort.save(userInterview);
                });
    }

    @Override
    public Mono<UserInterview> finishUserInterview
            (Flux<FilePart> audios, FilePart fullVideo, String userId, String interviewId, String durationMinutes) {

        String originalFilename = fullVideo.filename();
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String videoKey = "entrevista/" + UUID.randomUUID() + extension;

        return interviewRepositoryPort.findByUserIdAndInterviewId(userId, interviewId)
                .switchIfEmpty(Mono.error(new RuntimeException(
                        "UserInterview not found for userId=" + userId + " and interviewId=" + interviewId)))
                .flatMap(existingInterview ->
                        awsExternalServicePort.uploadFile(videoKey,fullVideo)
                                .flatMap(success -> {
                                    if (!success) {
                                        return Mono.error(new RuntimeException("Error al subir el video a S3"));
                                    }

                                    return getAnswers(audios)
                                            .flatMap(this::evaluateAnswerWithIA)
                                            .collectList()
                                            .flatMap(answers -> {
                                                int score = answers.stream()
                                                        .mapToInt(UserInterview.Answer::getPoints)
                                                        .sum();

                                                existingInterview.setS3KeyPath(videoKey);
                                                existingInterview.setDate(LocalDate.now());
                                                existingInterview.setAnswers(answers);
                                                existingInterview.setScore(score);
                                                existingInterview.setDuration(Integer.parseInt(durationMinutes));
                                                existingInterview.setState(EstadoEntrevista.COMPLETADA.name());

                                                return interviewRepositoryPort.save(existingInterview);
                                            });
                                })
                );
    }

    private Mono<String> makePrompt(String question, String answer) {
        String cleanQuestion = cleanInputText(question);
        String cleanAnswer = cleanInputText(answer);
        String prompt = """
                Evalúa esta respuesta de entrevista laboral.
                
                            INSTRUCCIONES ESTRICTAS:
                            1. Califica de 0 a 10 puntos
                            2. Si no responde la pregunta = 0 puntos
                            3. Ignora errores de escritura y no evalues por ello
                            4. Evalúa conocimiento técnico pero no muy estricto
                            5. Sé comprensivo con candidatos junior
                
                            PREGUNTA: %s
                            RESPUESTA: %s
                
                            RESPONDE SOLO CON ESTE JSON (sin texto antes ni después):
                            {"score":X,"justification":"texto de máximo 80 caracteres"}
                
                            IMPORTANTE:\s
                            - Poca explicación detallada y no uses caracteres especiales
                            - NO uses saltos de línea en justification
                            - NO uses comillas dobles dentro de justification
                            - El score debe ser un número entero del 0 al 10
                            - La justification debe ser concisa y puntual regular texto
                """
                .formatted(cleanQuestion, cleanAnswer);
        return Mono.just(prompt);
    }

    private String cleanInputText(String text) {
        if (text == null) return "";

        return text.replaceAll("[\r\n]+", " ")           // Saltos de línea -> espacios
                .replaceAll("\\s+", " ")              // Múltiples espacios -> uno solo
                .replaceAll("[\"\\'\\\\]", "")        // Quitar comillas y backslashes
                .replaceAll("[{}\\[\\]]", "")         // Quitar caracteres JSON problemáticos
                .trim();
    }

    private String sanitizeJson(String rawResponse) {
        if (rawResponse == null || rawResponse.trim().isEmpty()) {
            log.warn("⚠️ Respuesta IA vacía o nula");
            return "{\"score\":0,\"justification\":\"Respuesta vacía\"}";
        }

        try {
            // Paso 1: limpiar decoraciones (```json ... ```)
            String cleaned = rawResponse
                    .replaceAll("(?s)```json", "")
                    .replaceAll("(?s)```", "")
                    .trim();

            // Paso 2: validar parseo con Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(cleaned);

            int score = node.has("score") ? node.get("score").asInt(0) : 0;
            String justification = node.has("justification")
                    ? node.get("justification").asText().trim()
                    : "Sin justificación";

            // Limitar longitud
            if (justification.length() > 120) {
                justification = justification.substring(0, 117) + "...";
            }

            // Paso 3: devolver JSON limpio
            String result = String.format("{\"score\":%d,\"justification\":\"%s\"}", score, justification);
            log.debug("✅ JSON validado: {}", result);
            return result;

        } catch (Exception e) {
            log.error("❌ Error parseando respuesta IA: {}", rawResponse, e);
            return "{\"score\":0,\"justification\":\"Error procesando respuesta IA\"}";
        }
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
