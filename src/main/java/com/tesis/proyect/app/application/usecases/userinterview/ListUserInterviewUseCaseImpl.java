package com.tesis.proyect.app.application.usecases.userinterview;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.input.userinterview.ListUserInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListUserInterviewUseCaseImpl implements ListUserInterviewUseCase {

    private final UserInterviewRepositoryPort interviewRepositoryPort;

    @Override
    public Flux<UserInterview> findAll() {
        return interviewRepositoryPort.findAll(Sort.by(Sort.Direction.DESC, "score"));
    }
}
