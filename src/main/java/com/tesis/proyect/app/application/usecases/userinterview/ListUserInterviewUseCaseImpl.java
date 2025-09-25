package com.tesis.proyect.app.application.usecases.userinterview;

import com.tesis.proyect.app.domain.ports.input.userinterview.ListUserInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import com.tesis.proyect.app.infrastructure.dto.response.UserInterviewDetailResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
public class ListUserInterviewUseCaseImpl implements ListUserInterviewUseCase {

    private final UserInterviewRepositoryPort userInterviewRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final InterviewRepositoryPort interviewRepositoryPort;

    @Override
    public Flux<UserInterviewDetailResponse> findAll() {
        return userInterviewRepositoryPort.findAll()
                .flatMap(userInterview ->
                        userRepositoryPort.findById(userInterview.getUserId())
                                .zipWith(interviewRepositoryPort.findById(userInterview.getInterviewId()))
                                .map(tuple -> {
                                    var user = tuple.getT1();
                                    var interview = tuple.getT2();

                                    return UserInterviewDetailResponse.builder()
                                            .id(userInterview.getId())
                                            .score(userInterview.getScore())
                                            .state(userInterview.getState())
                                            .date(userInterview.getDate())
                                            .userId(userInterview.getUserId())
                                            .userName(user.getName() + " " + user.getLastName())
                                            .interviewId(userInterview.getInterviewId())
                                            .interviewTitle(interview.getTitle())
                                            .s3KeyPath(userInterview.getS3KeyPath())
                                            .duration(userInterview.getDuration())
                                            .answers(
                                                    Optional.ofNullable(userInterview.getAnswers())
                                                            .orElse(Collections.emptyList())
                                                            .stream()
                                                            .map(answer -> new UserInterviewDetailResponse.Answer(
                                                                    answer.getQuestionText(),
                                                                    answer.getResponseText(),
                                                                    answer.getPoints(),
                                                                    answer.getDescription()
                                                            ))
                                                            .toList()
                                            )
                                            .build();
                                })
                );
    }


    @Override
    public Mono<UserInterviewDetailResponse> findByUserId(String userId) {
        return userInterviewRepositoryPort.findByUserId(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no tiene entrevista realizada: " + userId)))
                .flatMap(userInterview ->
                        userRepositoryPort.findById(userInterview.getUserId())
                                .zipWith(interviewRepositoryPort.findById(userInterview.getInterviewId()))
                                .map(tuple -> {
                                    var user = tuple.getT1();
                                    var interview = tuple.getT2();

                                    return UserInterviewDetailResponse.builder()
                                            .id(userInterview.getId())
                                            .score(userInterview.getScore())
                                            .state(userInterview.getState())
                                            .date(userInterview.getDate())
                                            .userId(userInterview.getUserId())
                                            .userName(user.getName() + " " + user.getLastName())
                                            .interviewId(userInterview.getInterviewId())
                                            .interviewTitle(interview.getTitle())
                                            .s3KeyPath(userInterview.getS3KeyPath())
                                            .duration(userInterview.getDuration())
                                            .answers(
                                                    Optional.ofNullable(userInterview.getAnswers())
                                                            .orElse(Collections.emptyList())
                                                            .stream()
                                                            .map(answer -> new UserInterviewDetailResponse.Answer(
                                                                    answer.getQuestionText(),
                                                                    answer.getResponseText(),
                                                                    answer.getPoints(),
                                                                    answer.getDescription()
                                                            ))
                                                            .toList()
                                            )
                                            .build();
                                })
                );
    }

}
