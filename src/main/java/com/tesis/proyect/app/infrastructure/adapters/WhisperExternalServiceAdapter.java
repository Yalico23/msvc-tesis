package com.tesis.proyect.app.infrastructure.adapters;

import com.tesis.proyect.app.domain.ports.output.WhisperExternalServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class WhisperExternalServiceAdapter implements WhisperExternalServicePort {

    private final WebClient webClientBuilder;
    @Value("${openai.whisper.url}")
    private String whisperUrl;

    @Value("${openai.whisper.api-key}")
    private String apiKey;

    @Override
    public Mono<String> getTextFromFile(FilePart filePart) {
        if (filePart == null) {
            return Mono.error(new IllegalArgumentException("El archivo no puede ser nulo"));
        }

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", filePart)
                .contentType(MediaType.APPLICATION_OCTET_STREAM);
        bodyBuilder.part("model", "whisper-1");

        return webClientBuilder
                .post()
                .uri(whisperUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(bodyBuilder.build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("Error cliente Whisper: {}", body);
                                    return Mono.error(new RuntimeException("Error cliente Whisper: " + body));
                                }))
                .onStatus(status -> status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("Error servidor Whisper: {}", body);
                                    return Mono.error(new RuntimeException("Error servidor Whisper: " + body));
                                }))
                .bodyToMono(String.class)
                .doOnNext(resp -> log.info("Respuesta Whisper OK"))
                .doOnError(err -> log.error("Error llamando a Whisper API", err));
    }
}
