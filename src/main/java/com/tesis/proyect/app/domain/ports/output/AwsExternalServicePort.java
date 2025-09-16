package com.tesis.proyect.app.domain.ports.output;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface AwsExternalServicePort {
    Mono<Boolean> uploadFile(String key, FilePart fileLocation);
    Mono<String> generatePresigned(String key, Duration duration);
}