package com.tesis.proyect.app.domain.ports.input.user;

import com.tesis.proyect.app.domain.models.User;
import reactor.core.publisher.Mono;

public interface CreateUserUseCase {
    Mono<User> createUser(User user);
}
