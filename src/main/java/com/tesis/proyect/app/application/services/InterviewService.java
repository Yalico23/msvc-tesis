package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.domain.ports.input.interview.ListInterviewUseCase;
import com.tesis.proyect.app.domain.ports.input.interview.SaveInterviewUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InterviewService implements
        SaveInterviewUseCase,
        ListInterviewUseCase {

    private final SaveInterviewUseCase saveInterviewUseCase;
    private final ListInterviewUseCase listInterviewUseCase;

    public InterviewService(SaveInterviewUseCase saveInterviewUseCase, ListInterviewUseCase listInterviewUseCase) {
        this.saveInterviewUseCase = saveInterviewUseCase;
        this.listInterviewUseCase = listInterviewUseCase;
    }

    @Transactional
    @Override
    public Mono<Interview> saveInterview(Interview interview, String userId) {
        return saveInterviewUseCase.saveInterview(interview, userId);
    }

    @Transactional
    @Override
    public Mono<Interview> updateInterview(Interview interview, String userId) {
        return saveInterviewUseCase.updateInterview(interview,userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<Interview> listInterviewsByUserId(String userId) {
        return listInterviewUseCase.listInterviewsByUserId(userId);
    }
}
