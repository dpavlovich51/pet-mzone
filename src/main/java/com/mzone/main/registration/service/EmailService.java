package com.mzone.main.registration.service;

import com.mzone.main.registration.command.ConfirmRegistrationCodeCommand;
import com.mzone.main.registration.command.SendRegisterCodeCommand;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.event.RegistrationCodeSentEvent;
import com.mzone.main.registration.event.UserEmailRegistrationCodeConfirmedEvent;
import com.mzone.main.registration.entity.EmailRegistrationCodeEntity;

import java.util.Optional;

public interface EmailService {

    RegistrationCodeSentEvent sendRegistrationCode(SendRegisterCodeCommand command);

    Optional<EmailRegistrationCodeEntity> findFreshRegistrationCodeByEmailAndCode(EmailValue email, String code);

    UserEmailRegistrationCodeConfirmedEvent confirmEmailCode(ConfirmRegistrationCodeCommand command);

    Optional<EmailRegistrationCodeEntity> findFreshRegistrationCodeByEmail(EmailValue email);

}
