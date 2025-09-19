package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.ports.input.user.CreateUserUseCase;
import com.tesis.proyect.app.domain.ports.input.user.FindUserUSerCase;
import com.tesis.proyect.app.domain.ports.input.user.InterviewAsignedUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService implements CreateUserUseCase, FindUserUSerCase, InterviewAsignedUseCase {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserUSerCase findUserUSerCase;
    private final InterviewAsignedUseCase interviewAsignedUseCase;

    public UserService(CreateUserUseCase createUserUseCase, FindUserUSerCase findUserUSerCase, InterviewAsignedUseCase interviewAsignedUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserUSerCase = findUserUSerCase;
        this.interviewAsignedUseCase = interviewAsignedUseCase;
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

    @Transactional
    @Override
    public Mono<User> assignInterviewToUser(String userId, String interviewId) {
        return interviewAsignedUseCase.assignInterviewToUser(userId,interviewId);
    }
}
