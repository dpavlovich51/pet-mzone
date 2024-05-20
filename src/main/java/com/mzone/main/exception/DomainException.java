package com.mzone.main.exception;

/**
 * All exception connected with domain logic extends
 * {@link com.mzone.main.exception.DomainException}
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Exception e) {
        super(message, e);
    }
}
