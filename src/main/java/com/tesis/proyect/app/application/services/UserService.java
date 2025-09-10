package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.ports.input.user.CreateUserUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class UserService implements CreateUserUseCase {

    private final CreateUserUseCase createUserUseCase;

    public UserService(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @Transactional
    @Override
    public Mono<User> createUser(User user) {
        return createUserUseCase.createUser(user);
    }
}
