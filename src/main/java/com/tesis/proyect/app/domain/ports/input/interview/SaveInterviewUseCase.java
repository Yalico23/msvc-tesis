package com.tesis.proyect.app.domain.ports.input.interview;


import com.tesis.proyect.app.domain.models.Interview;
import reactor.core.publisher.Mono;

public interface SaveInterviewUseCase {
    Mono<Interview> saveInterview (Interview interview);
    Mono<Interview> updateInterview (Interview interview);
}
