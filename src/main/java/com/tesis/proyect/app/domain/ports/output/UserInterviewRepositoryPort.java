package com.tesis.proyect.app.domain.ports.output;

import com.tesis.proyect.app.domain.models.UserInterview;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserInterviewRepositoryPort {
    Mono<UserInterview> save (UserInterview userInterview);
    Mono<Boolean> existsByUserId(String userId);
    Flux<UserInterview> findAll(Sort sort);
    Mono<UserInterview> findByUserIdAndInterviewId(String userId, String interviewId);
}
