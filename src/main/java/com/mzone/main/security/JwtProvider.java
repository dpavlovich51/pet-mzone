package com.mzone.main.security;

import com.mzone.main.entity.UserRole;
import io.jsonwebtoken.JwtException;
import lombok.Data;

import java.util.Set;

public interface JwtProvider {

    //// TODO: 25/11/2021 move to nickname or id (if user doesn't set nickname (like in telegram))
    String createToken(String email, Set<UserRole> roles);

    JwtData parse(String token);

    JwtData parseRefreshToken(String token) throws JwtException;

    String createRefreshToken(String email, Set<UserRole> expectedRoles);

    @Data
    class JwtData {
        private final Headers headers;
        private final Body body;

        @Data
        public static class Body {
            private final String username;
            private final Set<UserRole> roles;
        }

        @Data
        public static class Headers {
            private final boolean isRefresh;
        }
    }

}
