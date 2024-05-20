package com.mzone.main.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.mzone.main.controller.UserController.SingInResponseDto;
import com.mzone.main.core.Gender;
import com.mzone.main.entity.UserRole;
import com.mzone.main.exception.GoogleIdTokenVerifierException;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.security.JwtProvider;
import com.mzone.main.user.entity.UserEntity;
import com.mzone.main.user.service.UserService;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;


@RestController
@AllArgsConstructor
public class GoogleAuthController {

    //// TODO: 23/11/2021 move to env vars
    public static final String GOOGLE_CLIENT_ID = "1081412449566-jokij4i3e6jpk6fs0q5aq2l5qon3l07r.apps.googleusercontent.com";

    private final UserService userService;
    private final JwtProvider jwtProvider;

/*
    {
    "givenName": "Roma",
    "familyName": "Dyachenko",
    "name": "Roma Dyachenko",
    "pictureUrl": "https://lh3.googleusercontent.com/a-/AOh14GihtY-5JGBNWNecvTRWSqzkkkLk-Kw-a8_QLNhN=s96-c",
    "locale": "ru",
    "payload": {
        "aud": "1081412449566-jokij4i3e6jpk6fs0q5aq2l5qon3l07r.apps.googleusercontent.com",
        "azp": "1081412449566-f2m38rhol4nsc42k2us4gu44s6rqn5ph.apps.googleusercontent.com",
        "email": "wizard.roma1990@gmail.com",
        "email_verified": true,
        "exp": 1637829326,
        "iat": 1637825726,
        "iss": "https://accounts.google.com",
        "sub": "108620085118953827109",
        "name": "Roma Dyachenko",
        "picture": "https://lh3.googleusercontent.com/a-/AOh14GihtY-5JGBNWNecvTRWSqzkkkLk-Kw-a8_QLNhN=s96-c",
        "given_name": "Roma",
        "family_name": "Dyachenko",
        "locale": "ru"
    },
    "emailVerified": true,
    "email": "wizard.roma1990@gmail.com",
    "userId": "108620085118953827109"
}
     */

    /**
     * https://developers.google.com/identity/sign-in/android/backend-auth
     */
    @PostMapping("/user/sing-in-with-google")
    public SingInResponseDto singInWithGoogle(@Valid @RequestBody SingInWithGoogleTokenDto dto)
            throws GeneralSecurityException, IOException {

        final GoogleAuthRawData googleAuthRawData = retrieveUserDataFromGoogle(dto.getIdToken());

        final UserEntity user = userService.findByEmail(googleAuthRawData.email())
                .orElseGet(() -> {
                    final UserEntity userEntity = UserEntity.create(UUID.randomUUID(),
                            googleAuthRawData.firstName(),
                            googleAuthRawData.lastName(),
                            Gender.UNKNOWN,
                            null,
                            googleAuthRawData.email(),
                            null,
                            Set.of(UserRole.USER));

                    return userService.save(userEntity);
                });

        return new SingInResponseDto(
                jwtProvider.createToken(user.getEmail(),
                        Collections.singleton(UserRole.USER)),
                jwtProvider.createRefreshToken(user.getEmail(),
                        Collections.singleton(UserRole.USER))
        );
    }

    private GoogleAuthRawData retrieveUserDataFromGoogle(String aIdToken) throws GeneralSecurityException, IOException {
        //// TODO: 24/11/2021 REMOVE IT after it will be implemented
        Sentry.captureMessage("IdToken: " + aIdToken, SentryLevel.WARNING);

        //// TODO: 25/11/2021 move to bean
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                //// TODO: 25/11/2021 create bean GsonFactory with time format
                new GsonFactory())

                .setAudience(Collections.singleton(GOOGLE_CLIENT_ID))
                .build();

        GoogleIdToken idToken = Optional.ofNullable(verifier.verify(aIdToken))
                .orElseThrow(GoogleIdTokenVerifierException::new);

        GoogleIdToken.Payload payload = idToken.getPayload();

//        GoogleIdToken.Payload payload = idToken.getPayload();
//        String userId = payload.getSubject();
//        String email = payload.getEmail();
//        boolean emailVerified = payload.getEmailVerified();

//        String name = (String) payload.get("name");
//        String pictureUrl = (String) payload.get("picture");
//        String locale = (String) payload.get("locale");
//        String familyName = (String) payload.get("family_name");
//        String givenName = (String) payload.get("given_name");

//        final Map<String, Object> payload1 = Map.of("payload", payload,
//                "userId", userId,
//                "email", email,
//                "emailVerified", emailVerified,
//                "name", name,
//                "pictureUrl", pictureUrl,
//                "locale", locale,
//                "familyName", familyName,
//                "givenName", givenName);

        //// TODO: 25/11/2021 discuss nullable fields and return Optional if you decide work with optional fields
        return new GoogleAuthRawData((String) payload.get("family_name"),
                (String) payload.get("given_name"),
                EmailValue.create(payload.getEmail()));
    }

    public record GoogleAuthRawData(String lastName, String firstName, EmailValue email) {
    }

    @Data
    public static class SingInWithGoogleTokenDto {
        @NotBlank(message = "Token can't be blank.")
        private String idToken;
    }

}
