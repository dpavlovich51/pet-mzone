package com.mzone.main.registration.controller;

import com.mzone.main.controller.UserController;
import com.mzone.main.exception.UserAlreadyExistsException;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.event.NewAccountSuccessfullyRegisteredEvent;
import com.mzone.main.registration.event.RegistrationCodeSentEvent;
import com.mzone.main.registration.event.UserEmailRegistrationCodeConfirmedEvent;
import com.mzone.main.registration.event.UserRequestedRegistrationCodeEvent;
import com.mzone.main.registration.event.UserConfirmedRegistrationCodeEvent;
import com.mzone.main.registration.event.UserSingUpEvent;
import com.mzone.main.registration.policy.ConfirmRegistrationPolicy;
import com.mzone.main.registration.policy.EmailSendRegistrationCodePolicy;
import com.mzone.main.registration.policy.UserRegistrationWithEmailPolicy;
import com.mzone.main.user.service.UserService;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/v1/sing-up")
public class SingUpController {

    private final EmailSendRegistrationCodePolicy emailSendRegistrationCodePolicy;
    private final ConfirmRegistrationPolicy confirmRegistrationCodePolicy;
    private final UserService userService;
    private final UserRegistrationWithEmailPolicy userRegistrationWithEmailPolicy;

    public SingUpController(EmailSendRegistrationCodePolicy emailSendRegistrationCodePolicy,
                            ConfirmRegistrationPolicy confirmRegistrationCodePolicy,
                            UserService userService,
                            UserRegistrationWithEmailPolicy userRegistrationWithEmailPolicy) {
        this.emailSendRegistrationCodePolicy = emailSendRegistrationCodePolicy;
        this.confirmRegistrationCodePolicy = confirmRegistrationCodePolicy;
        this.userService = userService;
        this.userRegistrationWithEmailPolicy = userRegistrationWithEmailPolicy;
    }

    //// TODO: 09/05/2022 refactor it - add policy
    @PostMapping("/with-email")
    public NewAccountSuccessfullyRegisteredEvent singUpWithEmail(@Valid @RequestBody SingUpWithEmailRequestDto dto) {
        return userRegistrationWithEmailPolicy.tryExecute(new UserSingUpEvent(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getGender(),
                dto.getBirthday(),
                EmailValue.create(dto.getEmail()),
                dto.getPassword()
        ));
    }

    //// TODO: 09/05/2022 refactor it - add policy
    @PostMapping("/check-email")
    public ResponseEntity<Object> checkNewEmail(@Valid @RequestBody UserController.CheckEmailRequestDto dto) {
        userService.findByEmail(EmailValue.create(dto.getEmail()))
                .ifPresent(it -> {
                    throw new UserAlreadyExistsException("Email is already taken.");
                });

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/send-registration-code")
    public RegistrationCodeSentEvent sendRegistrationCode(@Valid @RequestBody SendRegistrationCodeDto dto) {
        return emailSendRegistrationCodePolicy.tryExecute(new UserRequestedRegistrationCodeEvent(
                EmailValue.create(dto.getEmail())
        ));
    }

    @PostMapping("/confirm-email-with-code")
    public UserEmailRegistrationCodeConfirmedEvent confirmRegistrationWithCode(
            @Valid @RequestBody ConfirmRegistrationWithCodeDto dto) {
        return confirmRegistrationCodePolicy.tryExecute(new UserConfirmedRegistrationCodeEvent(
                EmailValue.create(dto.getEmail()),
                dto.getCode()
        ));
    }

    @Data
    private static class SendRegistrationCodeDto {
        @Email
        @NotBlank
        private String email;
    }

    @Data
    private static class ConfirmRegistrationWithCodeDto {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        @Size(min = 6)
        private String code;
    }

    @Data
    private static class SingUpWithEmailRequestDto {
        @Size(min = 2, max = 100, message = "Incorrect first name")
        @NotNull(message = "First name can't be null")
        private String firstName;
        @Size(min = 2, max = 100, message = "Incorrect last name")
        @NotNull(message = "Last name can't be null")
        private String lastName;
        @NotNull(message = "Gender can't be null")
        private Integer gender;
        @NotNull(message = "Birthday can't be null")
        private DateTime birthday;
        //        @NonNull //// TODO: 09/11/2021 https://mzone51.atlassian.net/browse/MZ-24
//        private String phoneNumber;
        @Email(message = "Incorrect email")
        @NotBlank(message = "Email can't be blank")
        private String email;
        @NotNull(message = "Incorrect password")
        private String password;
    }

}
