package com.tesis.proyect.app.domain.ports.output;

import com.tesis.proyect.app.domain.models.UserInterview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInterviewRepositoryPort {
    Mono<UserInterview> save (UserInterview userInterview);
    Mono<Boolean> existsByUserId(String userId);
    Mono<UserInterview> findByUserId(String userId);
    Flux<UserInterview> findAll();
    Mono<UserInterview> findByUserIdAndInterviewId(String userId, String interviewId);
    Mono<UserInterview> findByInterviewId(String Interviewid);
}
