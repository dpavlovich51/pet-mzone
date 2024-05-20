package com.mzone.main.security;

import com.mzone.main.entity.*;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.test.context.junit4.*;

import java.util.*;

@RunWith(SpringRunner.class)
@Import(JwtTestConfiguration.class)
public class DefaultJwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    public void whenValidToken_thenTokenShouldBeParsed() {
        final String expectedUsername = "username";
        final Set<UserRole> expectedRoles = Set.of(UserRole.USER);

        final String token = jwtProvider.createToken(expectedUsername, expectedRoles);
        final JwtProvider.JwtData parsedData = jwtProvider.parse(token);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedUsername, parsedData.getBody().getUsername()),
                () -> Assertions.assertFalse(parsedData.getHeaders().isRefresh()),
                () -> Assertions.assertIterableEquals(expectedRoles, parsedData.getBody().getRoles())
        );
    }

    @Test
    public void whenValidRefreshToken_thenRefreshTokenShouldBeParsed() {
        final String expectedUsername = "username";
        final Set<UserRole> expectedRoles = Set.of(UserRole.USER);

        final String token = jwtProvider.createRefreshToken(expectedUsername, expectedRoles);
        final JwtProvider.JwtData parsedData = jwtProvider.parseRefreshToken(token);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedUsername, parsedData.getBody().getUsername()),
                () -> Assertions.assertTrue(parsedData.getHeaders().isRefresh()),
                () -> Assertions.assertIterableEquals(expectedRoles, parsedData.getBody().getRoles())
        );
    }

}