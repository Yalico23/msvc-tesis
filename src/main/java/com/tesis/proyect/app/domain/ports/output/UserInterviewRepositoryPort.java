package com.tesis.proyect.app.domain.ports.output;

import com.tesis.proyect.app.domain.models.UserInterview;
import reactor.core.publisher.Mono;

public interface UserInterviewRepositoryPort {
    Mono<UserInterview> save (UserInterview userInterview);
}
