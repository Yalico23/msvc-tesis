package com.tesis.proyect.app.domain.ports.input.interview;

import com.tesis.proyect.app.domain.models.Interview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ListInterviewUseCase {
    Flux<Interview> listInterviews();
    Mono<Interview> findByUserIdAssigned(String userIdAssigned);
}
