package com.tesis.proyect.app.domain.ports.input.interview;

import reactor.core.publisher.Mono;

public interface DeleteInterviewUseCase {
    Mono<Void> deleteInterview(String interviewId);
}
