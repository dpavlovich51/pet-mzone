package com.mzone.main.exception;

public class InvalidRefreshTokenException extends DomainException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
