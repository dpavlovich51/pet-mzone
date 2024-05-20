package com.mzone.main.registration.policy;

import com.mzone.main.exception.UserAlreadyExistsException;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.service.EmailService;
import com.mzone.main.registration.command.SendRegisterCodeCommand;
import com.mzone.main.registration.event.RegistrationCodeSentEvent;
import com.mzone.main.registration.event.UserRequestedRegistrationCodeEvent;
import com.mzone.main.registration.exception.UserHasAlreadyRequestedRegistrationCodeException;
import com.mzone.main.user.service.UserService;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static com.mzone.main.registration.service.DefaultEmailService.CODE_EXPIRATION_MINUTES;
import static com.mzone.main.registration.service.DefaultEmailService.Utils.isRegistrationCodeFresh;

@Component
public class EmailSendRegistrationCodePolicy implements BusinessPolicyExecutor
        <RegistrationCodeSentEvent, UserRequestedRegistrationCodeEvent> {
    private final EmailService emailService;
    private final UserService userService;
    private final PolicyChecker policyChecker;

    public EmailSendRegistrationCodePolicy(EmailService emailService,
                                           UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
        policyChecker = new PolicyChecker();
    }

    @Override
    public RegistrationCodeSentEvent tryExecute(UserRequestedRegistrationCodeEvent event) {
        policyChecker.checkIfUserWithEmailAlreadyExists(event.getEmail());
        policyChecker.checkIfUserHasAlreadyFreshCode(event.getEmail());

        final SendRegisterCodeCommand command = new SendRegisterCodeCommand(
                event.getEmail()
        );
        return emailService.sendRegistrationCode(command);
    }

    private class PolicyChecker {

        private void checkIfUserHasAlreadyFreshCode(EmailValue email) {
            emailService.findFreshRegistrationCodeByEmail(email)
                    .filter(it -> isRegistrationCodeFresh(it.getCreatedTime()))
                    .ifPresent(it -> {
                        throw new UserHasAlreadyRequestedRegistrationCodeException(MessageFormat
                                .format("User has already requested registration code for email: {0} within {1} min.",
                                        email,
                                        CODE_EXPIRATION_MINUTES));
                    });
        }
        private void checkIfUserWithEmailAlreadyExists(EmailValue email) {
            userService.findByEmail(email)
                    .ifPresent(it -> {
                        throw new UserAlreadyExistsException("Email is already taken: " + email);
                    });
        }
    }
}
