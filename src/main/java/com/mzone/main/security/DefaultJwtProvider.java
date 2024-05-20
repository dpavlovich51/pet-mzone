package com.mzone.main.security;

import com.mzone.main.entity.UserRole;
import com.mzone.main.exception.InvalidRefreshTokenException;
import com.mzone.main.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mzone.main.security.DefaultJwtProvider.ClaimHelper.REFRESH_TOKEN_HEADER;

@Component
public class DefaultJwtProvider implements JwtProvider {

    private final Long refreshTokenExpirationTime;
    private final Long tokenExpirationTime;
    private String secret;

    public DefaultJwtProvider(@Value("${jwt.secret}") String secret,
                              @Value("${jwt.token.expiration}") Long tokenExpirationTime,
                              @Value("${jwt.refresh-token.expiration}") Long refreshTokenExpirationTime) {
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.secret = secret;
        this.tokenExpirationTime = tokenExpirationTime;
    }

    @PostConstruct
    public void init() {
        secret = new String(Base64.getEncoder().encode(secret.getBytes()));
    }

    @Override
    public String createToken(String email, Set<UserRole> roles) {
        return jwtBuilder(email, roles, tokenExpirationTime)
                .build(secret);
    }

    private JwtInnerBuilder jwtBuilder(String username,
                                       Set<UserRole> roles,
                                       Long expTimeMilliseconds) {
        return JwtInnerBuilder.body(new JwtData.Body(username, roles))
                .expiredAfter(expTimeMilliseconds);
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
    }

    @Override
    public JwtData parse(String token) {
        final JwtData jwtData = parse0(token);

        if (jwtData.getHeaders().isRefresh()) {
            throw new InvalidRefreshTokenException("Invalid token.");
        }
        return jwtData;
    }

    @Override
    public JwtData parseRefreshToken(String token) throws JwtException {
        final JwtData jwtData = parse0(token);

        if (!jwtData.getHeaders().isRefresh()) {
            throw new InvalidRefreshTokenException("Invalid token.");
        }
        return jwtData;
    }

    public JwtData parse0(String token) {
        if (token.isEmpty()) {
            throw new JwtAuthenticationException("Token is empty.");
        }
        try {
            final Jws<Claims> claims = parseClaims(token);

            if (claims.getSignature().isEmpty()) {
                throw new JwtAuthenticationException("Token's: " + token + " signature is empty.");
            }

            final String username = claims.getBody().getSubject();
            final Set<UserRole> roles = ClaimHelper.getUserRoles(claims.getBody());
            return new JwtData(
                    new JwtData.Headers(ClaimHelper.isRefreshToken(claims)),
                    new JwtData.Body(username, roles)
            );
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("Token: " + token + " is expired.", e);
        }
    }

    @Override
    public String createRefreshToken(String email,
                                     Set<UserRole> expectedRoles) {
        return jwtBuilder(email, expectedRoles, refreshTokenExpirationTime)
                .refresh()
                .build(secret);
    }

    record ClaimHelper(Claims claims) {

        public static final String REFRESH_TOKEN_HEADER = "refresh";

        private static final String ROLE_MAP_KEY = "roles";

        public static Set<UserRole> getUserRoles(Claims claims) {
            return ((Collection<String>) claims.get(ROLE_MAP_KEY)).stream()
                    .map(UserRole::valueOf)
                    .collect(Collectors.toSet());
        }

        public static ClaimHelper fromUsername(String username) {
            return new ClaimHelper(
                    Jwts.claims()
                            .setSubject(username)
            );
        }

        public static boolean isRefreshToken(Jws<Claims> claims) {
            return (boolean) claims.getHeader().get(REFRESH_TOKEN_HEADER);
        }

        public ClaimHelper setAuthorities(Set<UserRole> roles) {
            claims.put(ROLE_MAP_KEY, roles);
            return this;
        }

        public Claims build() {
            return claims;
        }
    }

    @Data
    private static class JwtInnerBuilder {

        private final Date issuedAt;
        private final JwtBuilder jwtBuilder;

        public static JwtInnerBuilder body(JwtData.Body body) {
            final Claims claims = ClaimHelper.fromUsername(body.getUsername())
                    .setAuthorities(body.getRoles())
                    .build();

            final Date issuedAt = new Date();
            final JwtBuilder jwtBuilder = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(issuedAt)
                    .setHeaderParam(REFRESH_TOKEN_HEADER, false);

            return new JwtInnerBuilder(issuedAt, jwtBuilder);
        }

        public JwtInnerBuilder refresh() {
            jwtBuilder.setHeaderParam(REFRESH_TOKEN_HEADER, true);
            return this;
        }

        public JwtInnerBuilder expiredAfter(long milliseconds) {
            Date expDate = new Date(issuedAt.getTime() + milliseconds);
            jwtBuilder.setExpiration(expDate);
            return this;
        }

        public String build(String secret) {
            //// TODO: 28/08/2021 remove deprecated usages
            //// TODO: 28/08/2021 and choose good SignatureAlgorithm
            return jwtBuilder.signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        }
    }
}
