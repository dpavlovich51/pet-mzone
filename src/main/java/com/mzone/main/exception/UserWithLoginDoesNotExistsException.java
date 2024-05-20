package com.mzone.main.exception;

public class UserWithLoginDoesNotExistsException extends DomainException {
    public UserWithLoginDoesNotExistsException(String message) {
        super(message);
    }
}
