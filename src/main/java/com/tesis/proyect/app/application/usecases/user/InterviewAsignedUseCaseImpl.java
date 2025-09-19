package com.tesis.proyect.app.application.usecases.user;

import com.tesis.proyect.app.domain.exceptions.UserNotFoundException;
import com.tesis.proyect.app.domain.models.User;
import com.tesis.proyect.app.domain.ports.input.user.InterviewAsignedUseCase;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class InterviewAsignedUseCaseImpl implements InterviewAsignedUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Mono<User> assignInterviewToUser(String userId, String interviewId) {
        return userRepositoryPort.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with id: " + userId)))
                .flatMap(user -> {
                    user.setInterviewAsignedId(interviewId);
                    return userRepositoryPort.save(user);
                });
    }
}
