package com.tesis.proyect.app.domain.ports.input.userinterview;

import com.tesis.proyect.app.domain.models.UserInterview;
import reactor.core.publisher.Flux;

public interface ListUserInterviewUseCase {
    Flux<UserInterview> listAllUserInterviews(String idInterview);
}
