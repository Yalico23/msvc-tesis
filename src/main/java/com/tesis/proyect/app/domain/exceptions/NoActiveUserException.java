package com.tesis.proyect.app.domain.exceptions;

public class NoActiveUserException extends RuntimeException {
    public NoActiveUserException(String message) {
        super(message);
    }
}
