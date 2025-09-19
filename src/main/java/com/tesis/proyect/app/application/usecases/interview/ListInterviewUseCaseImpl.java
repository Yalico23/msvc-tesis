package com.tesis.proyect.app.application.usecases.interview;

import com.tesis.proyect.app.domain.exceptions.UserNotFoundException;
import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.domain.ports.input.interview.ListInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ListInterviewUseCaseImpl implements ListInterviewUseCase {

    private final InterviewRepositoryPort repositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Flux<Interview> listInterviews() {
        return repositoryPort.findAll();
    }

    @Override
    public Mono<Interview> findByUserIdAssigned(String userIdAssigned) {
        return userRepositoryPort.findById(userIdAssigned)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with id: " + userIdAssigned)))
                .flatMap(user -> repositoryPort.findById(user.getInterviewAsignedId()));
    }
}
