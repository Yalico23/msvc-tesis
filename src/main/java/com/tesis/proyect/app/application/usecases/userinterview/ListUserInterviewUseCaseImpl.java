package com.tesis.proyect.app.application.usecases.userinterview;

import com.tesis.proyect.app.domain.models.UserInterview;
import com.tesis.proyect.app.domain.ports.input.userinterview.ListUserInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListUserInterviewUseCaseImpl implements ListUserInterviewUseCase {

    private final UserInterviewRepositoryPort interviewRepositoryPort;

    @Override
    public Flux<UserInterview> listAllUserInterviews(String idInterview) {
        return interviewRepositoryPort.findAllByInterviewId(idInterview);
    }
}
