package com.mzone.main.registration.exception;

import com.mzone.main.exception.DomainException;

public class UserHasAlreadyRequestedRegistrationCodeException extends DomainException {
    public UserHasAlreadyRequestedRegistrationCodeException(String message) {
        super(message);
    }
}
