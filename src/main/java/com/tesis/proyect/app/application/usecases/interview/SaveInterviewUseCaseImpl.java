package com.tesis.proyect.app.application.usecases.interview;

import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.domain.ports.input.interview.SaveInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class SaveInterviewUseCaseImpl implements SaveInterviewUseCase {

    private final InterviewRepositoryPort interviewRepositoryPort;

    @Override
    public Mono<Interview> saveInterview(Interview interview) {
        return Mono.just(interview)
                .map(i -> {
                    i.setCreatedAt(LocalDate.now());
                    i.getQuestions().forEach(q -> q.setId(UUID.randomUUID().toString().substring(0, 15)));
                    return i;
                })
                .flatMap(interviewRepositoryPort::save);
    }

    @Override
    public Mono<Interview> updateInterview(Interview interview) {
        return Mono.just(interview)
                .flatMap(interviewRepositoryPort::save);
    }
}
