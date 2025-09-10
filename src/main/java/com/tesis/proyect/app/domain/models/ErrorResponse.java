package com.tesis.proyect.app.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final List<String> details;
    private final LocalDateTime timestamp;
}
