package com.tesis.proyect.app.domain.ports.output;


import com.tesis.proyect.app.domain.models.Interview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InterviewRepositoryPort {
    Mono<Interview> save (Interview interview);
    Flux<Interview> findAllByUserId(String userId);
}
