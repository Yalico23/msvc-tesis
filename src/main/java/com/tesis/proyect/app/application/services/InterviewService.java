package com.tesis.proyect.app.application.services;

import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.domain.ports.input.interview.DeleteInterviewUseCase;
import com.tesis.proyect.app.domain.ports.input.interview.ListInterviewUseCase;
import com.tesis.proyect.app.domain.ports.input.interview.SaveInterviewUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InterviewService implements
        SaveInterviewUseCase,
        ListInterviewUseCase,
        DeleteInterviewUseCase {

    private final SaveInterviewUseCase saveInterviewUseCase;
    private final ListInterviewUseCase listInterviewUseCase;
    private final DeleteInterviewUseCase deleteInterviewUseCase;;

    public InterviewService(SaveInterviewUseCase saveInterviewUseCase, ListInterviewUseCase listInterviewUseCase, DeleteInterviewUseCase deleteInterviewUseCase) {
        this.saveInterviewUseCase = saveInterviewUseCase;
        this.listInterviewUseCase = listInterviewUseCase;
        this.deleteInterviewUseCase = deleteInterviewUseCase;
    }

    @Transactional
    @Override
    public Mono<Interview> saveInterview(Interview interview) {
        return saveInterviewUseCase.saveInterview(interview);
    }

    @Transactional
    @Override
    public Mono<Interview> updateInterview(Interview interview) {
        return saveInterviewUseCase.updateInterview(interview);
    }

    @Transactional(readOnly = true)
    @Override
    public Flux<Interview> listInterviews() {
        return listInterviewUseCase.listInterviews();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Interview> findByUserIdAssigned(String userIdAssigned) {
        return listInterviewUseCase.findByUserIdAssigned(userIdAssigned);
    }

    @Transactional
    @Override
    public Mono<Void> deleteInterview(String interviewId) {
        return deleteInterviewUseCase.deleteInterview(interviewId);
    }
}
