package com.tesis.proyect.app.infrastructure.dto.response;

import java.time.LocalDate;

public record ListUserInterviewResponse(
        Integer score,
        String state,
        LocalDate date,
        String userId,
        String interviewId
) {
}
