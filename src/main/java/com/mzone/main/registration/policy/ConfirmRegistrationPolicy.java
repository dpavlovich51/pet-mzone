package com.mzone.main.registration.policy;

import com.mzone.main.registration.service.EmailService;
import com.mzone.main.registration.command.ConfirmRegistrationCodeCommand;
import com.mzone.main.registration.event.UserConfirmedRegistrationCodeEvent;
import com.mzone.main.registration.event.UserEmailRegistrationCodeConfirmedEvent;
import com.mzone.main.registration.exception.RegistrationCodeNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ConfirmRegistrationPolicy implements BusinessPolicyExecutor
        <UserEmailRegistrationCodeConfirmedEvent, UserConfirmedRegistrationCodeEvent> {

    private final EmailService emailService;

    ConfirmRegistrationPolicy(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public UserEmailRegistrationCodeConfirmedEvent tryExecute(UserConfirmedRegistrationCodeEvent event) {
        return emailService.findFreshRegistrationCodeByEmailAndCode(event.getEmail(), event.getCode())
                .map(it -> emailService.confirmEmailCode(new ConfirmRegistrationCodeCommand(it.getId())))
                .orElseThrow(() -> new RegistrationCodeNotFoundException("Failed to find fresh registration code with email: " + event.getEmail()));
    }
}
