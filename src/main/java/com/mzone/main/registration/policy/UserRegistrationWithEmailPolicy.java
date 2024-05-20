package com.mzone.main.registration.policy;

import com.mzone.main.core.Gender;
import com.mzone.main.entity.UserRole;
import com.mzone.main.exception.UserAlreadyExistsException;
import com.mzone.main.registration.command.CreateNewAccountCommand;
import com.mzone.main.registration.entity.EmailRegistrationCodeEntity;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.event.NewAccountSuccessfullyRegisteredEvent;
import com.mzone.main.registration.event.UserSingUpEvent;
import com.mzone.main.registration.exception.RegistrationCodeNotFoundException;
import com.mzone.main.registration.service.EmailService;
import com.mzone.main.user.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class UserRegistrationWithEmailPolicy implements BusinessPolicyExecutor
        <NewAccountSuccessfullyRegisteredEvent, UserSingUpEvent> {

    private final UserService userService;
    private final EmailService emailService;

    public UserRegistrationWithEmailPolicy(UserService userService,
                                           EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public NewAccountSuccessfullyRegisteredEvent tryExecute(UserSingUpEvent event) {
        checkIfUserExists(event.getEmail());
        checkIfConfirmationCode(event.getEmail());

        return userService.createAccount(new CreateNewAccountCommand(
                UUID.randomUUID(),
                event.getFirstName(),
                event.getLastName(),
                Gender.VALUES[event.getGender()],
                event.getBirthday(),
                event.getEmail(),
                event.getPassword(),
                Set.of(UserRole.USER)
        ));
    }

    private void checkIfConfirmationCode(EmailValue email) {
        emailService.findFreshRegistrationCodeByEmail(email)
                .filter(EmailRegistrationCodeEntity::getIsConfirmed)
                .orElseThrow(() -> new RegistrationCodeNotFoundException("Failed to find registration code with email: " + email));
    }

    private void checkIfUserExists(EmailValue email) {
        userService.findByEmail(email)
                .ifPresent(it -> {
                    throw new UserAlreadyExistsException("User is already exist");
                });
    }
}
