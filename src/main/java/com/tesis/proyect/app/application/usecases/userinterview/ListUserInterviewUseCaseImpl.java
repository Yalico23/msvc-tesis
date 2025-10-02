package com.tesis.proyect.app.application.usecases.userinterview;

import com.tesis.proyect.app.domain.exceptions.UserDidInterviewException;
import com.tesis.proyect.app.domain.exceptions.UserDidNotInterviewException;
import com.tesis.proyect.app.domain.ports.input.userinterview.ListUserInterviewUseCase;
import com.tesis.proyect.app.domain.ports.output.AwsExternalServicePort;
import com.tesis.proyect.app.domain.ports.output.InterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserInterviewRepositoryPort;
import com.tesis.proyect.app.domain.ports.output.UserRepositoryPort;
import com.tesis.proyect.app.infrastructure.dto.response.UserInterviewDetailResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

public class ListUserInterviewUseCaseImpl implements ListUserInterviewUseCase {

    private final UserInterviewRepositoryPort userInterviewRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final InterviewRepositoryPort interviewRepositoryPort;
    private final AwsExternalServicePort awsExternalServicePort;

    public ListUserInterviewUseCaseImpl(UserInterviewRepositoryPort userInterviewRepositoryPort, UserRepositoryPort userRepositoryPort, InterviewRepositoryPort interviewRepositoryPort, AwsExternalServicePort awsExternalServicePort) {
        this.userInterviewRepositoryPort = userInterviewRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.interviewRepositoryPort = interviewRepositoryPort;
        this.awsExternalServicePort = awsExternalServicePort;
    }

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
                // error si no hay entrevista
                .switchIfEmpty(Mono.error(new UserDidNotInterviewException("Usuario no tiene entrevista realizada: " + userId)))
                // manejar si state = PENDING
                .flatMap(userInterview -> {
                    if ("PENDIENTE".equals(userInterview.getState())) {
                        return Mono.error(new UserDidNotInterviewException("Usuario aún no dio su entrevista"));
                    }

                    // flujo normal si no está en PENDING
                    return userRepositoryPort.findById(userInterview.getUserId())
                            .zipWith(interviewRepositoryPort.findById(userInterview.getInterviewId()))
                            .flatMap(tuple -> {
                                var user = tuple.getT1();
                                var interview = tuple.getT2();

                                return awsExternalServicePort
                                        .generatePresigned(userInterview.getS3KeyPath(), Duration.ofHours(2))
                                        .map(presignedUrl ->
                                                UserInterviewDetailResponse.builder()
                                                        .id(userInterview.getId())
                                                        .score(userInterview.getScore())
                                                        .state(userInterview.getState())
                                                        .date(userInterview.getDate())
                                                        .userId(userInterview.getUserId())
                                                        .userName(user.getName() + " " + user.getLastName())
                                                        .interviewId(userInterview.getInterviewId())
                                                        .interviewTitle(interview.getTitle())
                                                        .s3KeyPath(presignedUrl)
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
                                                        .build()
                                        );
                            });
                });
    }
}
