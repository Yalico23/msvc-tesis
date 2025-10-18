package com.tesis.proyect.app.domain.ports.input.user;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.infrastructure.dto.response.ListPartResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindUserUSerCase {
    Mono<User> findByEmail(String email);
    Flux<User> findByRoleName(String roleName);
    Flux<ListPartResponse> findByRolNameAnsStatus();
}
