package com.tesis.proyect.app.infrastructure.adapters;

import com.tesis.proyect.app.domain.ports.output.WhisperExternalServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class WhisperExternalServiceAdapter implements WhisperExternalServicePort {

    private final WebClient webClient;
    @Value("${openai.whisper.url}")
    private String whisperUrl;

    @Override
    public Mono<String> getTextFromFile(FilePart filePart) {
        if (filePart == null) {
            return Mono.error(new IllegalArgumentException("El archivo no puede ser nulo"));
        }
        return webClient.post()
                .uri(whisperUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("audio_file", filePart))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error cliente Whisper: " + body)))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Error servidor Whisper: " + body)))
                )
                .bodyToMono(String.class);
    }
}
