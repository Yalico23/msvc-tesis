package com.tesis.proyect.app.domain.ports.input.userinterview;

import com.tesis.proyect.app.infrastructure.dto.response.UserInterviewDetailResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ListUserInterviewUseCase {
    Flux<UserInterviewDetailResponse> findAll();
    Mono<UserInterviewDetailResponse> findByUserId(String userId);
}
