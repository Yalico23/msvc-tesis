package com.tesis.proyect.app.domain.ports.input.user;

import com.tesis.proyect.app.domain.models.User;
import reactor.core.publisher.Mono;

public interface InterviewAsignedUseCase {
    Mono<User> assignInterviewToUser(String userId, String interviewId);
}
