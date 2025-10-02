package com.tesis.proyect.app.application.usecases.interview;

import com.tesis.proyect.app.domain.exceptions.InterviewNotAssignedException;
import com.tesis.proyect.app.domain.exceptions.UserDidInterviewException;
import com.tesis.proyect.app.domain.exceptions.UserNotFoundException;
import com.tesis.proyect.app.domain.models.Interview;
import com.tesis.proyect.app.domain.ports.input.interview.ListInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class ListInterviewUseCaseImpl implements ListInterviewUseCase {

    private final InterviewRepositoryPort repositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final UserInterviewRepositoryPort interviewRepositoryPort;

    @Override
    public Flux<Interview> listInterviews() {
        return repositoryPort.findAll();
    }

    @Override
    public Mono<Interview> findByUserIdAssigned(String userId) {
        return userRepositoryPort.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with id: " + userId)))
                .flatMap(user -> {
                    if (user.getInterviewAsignedId() == null) {
                        return Mono.error(new InterviewNotAssignedException(
                                "User with id: " + userId + " has no interview assigned"));
                    }
                    return interviewRepositoryPort.findByUserId(userId)
                            .flatMap(userInterview -> {
                                if (!userInterview.getAnswers().isEmpty()) {
                                    return Mono.error(new UserDidInterviewException("User already did the interview with id: " + userId));
                                }
                                return repositoryPort.findById(user.getInterviewAsignedId());
                            });
                });
    }
}
