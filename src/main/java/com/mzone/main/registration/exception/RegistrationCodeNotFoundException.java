package com.mzone.main.registration.exception;

import com.mzone.main.exception.DomainException;

public class RegistrationCodeNotFoundException extends DomainException {
    public RegistrationCodeNotFoundException(String message) {
        super(message);
    }
}
