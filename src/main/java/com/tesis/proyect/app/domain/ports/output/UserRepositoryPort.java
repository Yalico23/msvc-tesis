package com.tesis.proyect.app.domain.ports.output;

import com.tesis.proyect.app.domain.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryPort {
    Mono<User> save (User user);
    Mono<Boolean> existByToken(String token);
    Mono<User> findByEmail(String email);
    Mono<Boolean> existByEmail(String email);
    Flux<User> findByRoleName(String roleName);
    Mono<User> findById(String id);
}
