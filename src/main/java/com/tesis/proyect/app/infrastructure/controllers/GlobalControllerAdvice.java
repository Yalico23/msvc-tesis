package com.tesis.proyect.app.infrastructure.controllers;

import com.tesis.proyect.app.domain.exceptions.InvalidCredentialsException;
import com.tesis.proyect.app.domain.exceptions.NoActiveUserException;
import com.tesis.proyect.app.domain.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ErrorResponse handleInvalidCredentials(InvalidCredentialsException ex) {
        return ErrorResponse.builder()
                .code("Unauthorized 401")
                .message(ex.getMessage())
                .details(List.of("El email o la contraseña son incorrectos"))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoActiveUserException.class)
    public ErrorResponse handleInvalidCredentials(NoActiveUserException ex) {
        return ErrorResponse.builder()
                .code("Unauthorized 401")
                .message(ex.getMessage())
                .details(List.of("El estado del usuario no es activo"))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return ErrorResponse.builder()
                .code("Forbidden 403")
                .message("Access denied")
                .details(List.of("No tienes permisos suficientes para acceder a este recurso"))
                .timestamp(LocalDateTime.now())
                .build();
    }
    // Falla
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({io.jsonwebtoken.ExpiredJwtException.class,
            io.jsonwebtoken.JwtException.class,
            io.jsonwebtoken.MalformedJwtException.class})
    public ErrorResponse handleJwtException(Exception ex) {
        return ErrorResponse.builder()
                .code("Unauthorized 401")
                .message("Invalid or expired token")
                .details(List.of("El token JWT es inválido o ha expirado"))
                .timestamp(LocalDateTime.now())
                .build();
    }

}
