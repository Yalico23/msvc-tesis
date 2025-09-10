package com.tesis.proyect.app.application.usecases.user;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.ports.input.user.FindUserUSerCase;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindUserUSerCaseImpl implements FindUserUSerCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepositoryPort.findByEmail(email);
    }
}
