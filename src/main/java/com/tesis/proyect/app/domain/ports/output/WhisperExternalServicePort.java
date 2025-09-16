package com.tesis.proyect.app.domain.ports.output;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface WhisperExternalServicePort {
    Mono<String> getTextFromFile(FilePart videoBytes);

}
