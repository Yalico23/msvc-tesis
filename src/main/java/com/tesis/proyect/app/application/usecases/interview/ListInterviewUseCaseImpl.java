package com.tesis.proyect.app.application.usecases.interview;

import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.domain.ports.input.interview.ListInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListInterviewUseCaseImpl implements ListInterviewUseCase {

    private final InterviewRepositoryPort repositoryPort;

    @Override
    public Flux<Interview> listInterviewsByUserId(String userId) {
        return repositoryPort.findAllByUserId(userId);
    }
}
