package com.mzone.main.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.core.*;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg) {
        super(msg);
    }

    public JwtAuthenticationException(String msg, ExpiredJwtException ex) {
        super(msg, ex);
    }
}
