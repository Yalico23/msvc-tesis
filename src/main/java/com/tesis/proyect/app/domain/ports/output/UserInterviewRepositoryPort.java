package com.tesis.proyect.app.domain.ports.output;

import com.tesis.proyect.app.domain.models.UserInterview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInterviewRepositoryPort {
    Mono<UserInterview> save (UserInterview userInterview);
    Flux<UserInterview> findAll();
    Flux<UserInterview> findAllByInterviewId(String interviewId);
    Mono<Boolean> existsByUserId(String userId);
}
