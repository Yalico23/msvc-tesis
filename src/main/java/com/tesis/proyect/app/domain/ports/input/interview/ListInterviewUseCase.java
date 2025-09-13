package com.tesis.proyect.app.domain.ports.input.interview;

import com.tesis.proyect.app.domain.models.Interview;
import reactor.core.publisher.Flux;

public interface ListInterviewUseCase {
    Flux<Interview> listInterviewsByUserId(String userId);
}
