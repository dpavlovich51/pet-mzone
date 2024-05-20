package com.mzone.main.security;

import com.mzone.main.security.JwtProvider.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtProvider jwtProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtData jwtData = jwtProvider.parse(authentication.getCredentials().toString());

        return new UsernamePasswordAuthenticationToken(
                jwtData.getBody().getUsername(),
                null,
                jwtData.getBody().getRoles()
        );
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Authentication.class.isAssignableFrom(aClass);
    }

}
