package com.tesis.proyect.app.domain.ports.output;

import reactor.core.publisher.Mono;

public interface IAExternalServicePort {
    Mono<String> getIaServiceResponse(String prompt);
}
