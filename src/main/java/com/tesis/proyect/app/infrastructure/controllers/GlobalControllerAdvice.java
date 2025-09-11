package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.domain.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        return ErrorResponse.builder()
                .code("InternalServerError 500")
                .message("An unexpected error occurred")
                .details(List.of(e.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ErrorResponse handleValidationException(WebExchangeBindException exception) {
        return ErrorResponse.builder()
                .code("BadRequest 400")
                .message("Validation failed for one or more fields")
                .details(
                        exception.getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .toList()
                )
                .timestamp(LocalDateTime.now())
                .build();
    }
}
