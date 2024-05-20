package com.mzone.main.exception;

public class GoogleIdTokenVerifierException extends DomainException {
    public GoogleIdTokenVerifierException() {
        super("Failed to verify IdToken.");
    }
}
