package com.mzone.main.controller;

import com.mzone.main.entity.UserRole;
import com.mzone.main.exception.DomainException;
import com.mzone.main.exception.InvalidRefreshTokenException;
import com.mzone.main.exception.UserWithLoginDoesNotExistsException;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.security.JwtProvider;
import com.mzone.main.user.entity.UserEntity;
import com.mzone.main.user.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/sing-in-with-email")
    public SingInResponseDto singInWithEmail(@Valid @RequestBody SingInRequestDto dto) {
        final UserEntity user = userService.findByEmail(EmailValue.create(dto.getEmail()))
                .filter(it -> it.getPassword().equals(dto.getPassword()))
                .orElseThrow(() -> new UserWithLoginDoesNotExistsException("Failed to login."));

        return new SingInResponseDto(
                jwtProvider.createToken(user.getEmail(),
                        Collections.singleton(UserRole.USER)),
                jwtProvider.createRefreshToken(user.getEmail(),
                        Collections.singleton(UserRole.USER))
        );
    }

    @PostMapping("/sing-in-with-refresh-token")
    public SingInResponseDto singInWithRefreshToken(@Valid @RequestBody SingInWithRefreshTokenDto dto) {
        try {
            final JwtProvider.JwtData tokenData = jwtProvider.parseRefreshToken(dto.getRefreshToken());

            final UserEntity user = userService.findByEmail(EmailValue.create(tokenData.getBody().getUsername()))
                    .orElseThrow(() -> new InvalidRefreshTokenException("Failed to find user with token: " + dto.getRefreshToken()));

            return new SingInResponseDto(
                    jwtProvider.createToken(user.getEmail(),
                            Collections.singleton(UserRole.USER)),
                    jwtProvider.createRefreshToken(user.getEmail(),
                            Collections.singleton(UserRole.USER))
            );
        } catch (JwtException ex) {
            throw new DomainException("Failed to refresh token.", ex);
        }
    }

    @Data
    private static class SingInRequestDto {
        @NotNull
        private String email;
        @NotNull
        private String password;
    }

    @Data
    public static class SingInResponseDto {
        @NonNull
        private String token;
        @NonNull
        private String refreshToken;
    }

    @Data
    public static class SingInWithRefreshTokenDto {
        @NotBlank(message = "Refresh token can't be blank.")
        private String refreshToken;
    }

    @Data
    public static class CheckEmailRequestDto {
        @NotBlank(message = "Email can't be blank")
        @Email(message = "Email is invalid.")
        private String email;
    }
}
