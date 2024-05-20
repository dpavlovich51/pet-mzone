package com.mzone.main.security;

import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.*;

@TestConfiguration
public class JwtTestConfiguration {

    @Bean
    public JwtProvider jwtProvider() {
        return new DefaultJwtProvider("aa5d5caabc994507bcecd07ace0dbd43",
                7200000L,
                146880000L
        );
    }

}
