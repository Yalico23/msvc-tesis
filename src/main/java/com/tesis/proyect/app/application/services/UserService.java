package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.ports.input.user.CreateUserUseCase;
import com.tesis.proyect.app.domain.ports.input.user.FindUserUSerCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements CreateUserUseCase, FindUserUSerCase {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserUSerCase findUserUSerCase;

    public UserService(CreateUserUseCase createUserUseCase, FindUserUSerCase findUserUSerCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserUSerCase = findUserUSerCase;
    }


    @Transactional
    @Override
    public Mono<User> createUser(User user) {
        return createUserUseCase.createUser(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<User> findByEmail(String email) {
        return findUserUSerCase.findByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<User> findByRoleName(String roleName) {
        return findUserUSerCase.findByRoleName(roleName);
    }
}
