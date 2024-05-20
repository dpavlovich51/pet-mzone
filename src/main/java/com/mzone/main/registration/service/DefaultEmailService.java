package com.mzone.main.registration.service;

import com.mzone.main.core.UTime;
import com.mzone.main.registration.command.ConfirmRegistrationCodeCommand;
import com.mzone.main.registration.command.SendRegisterCodeCommand;
import com.mzone.main.registration.entity.EmailRegistrationCodeEntity;
import com.mzone.main.registration.entity.EmailValue;
import com.mzone.main.registration.event.RegistrationCodeSentEvent;
import com.mzone.main.registration.event.UserEmailRegistrationCodeConfirmedEvent;
import com.mzone.main.registration.exception.RegistrationCodeNotFoundException;
import com.mzone.main.registration.repository.EmailRegistrationCodeRepository;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.StringWriter;
import java.util.Optional;
import java.util.UUID;


@Service
public class DefaultEmailService implements EmailService {

    //// TODO: 23/05/2022 move to config
    public static final int CODE_EXPIRATION_MINUTES = 3;

    //// TODO: 23/05/2022 move to config
    private static final String APP_EMAIL_ADDRESS = "meet.zone.app@gmail.com";
    //// TODO: 23/05/2022 move to config
    private static final String EMAIL_SUBJECT_TEXT = "Meet Zone";
    private static final String EMAIL_TEMPLATES = "/META-INF/email-templates";
    private static final String REGISTRATION_CODE_EMAIL_TEMPLATE = EMAIL_TEMPLATES + "/registration-code.html";

    private final JavaMailSender mailSender;
    private final EmailRegistrationCodeRepository emailRegistrationCodeRepository;
    private final VelocityEngine velocityEngine;

    public DefaultEmailService(JavaMailSender mailSender,
                               EmailRegistrationCodeRepository emailRegistrationCodeRepository,
                               VelocityEngine velocityEngine) {
        this.mailSender = mailSender;
        this.emailRegistrationCodeRepository = emailRegistrationCodeRepository;
        this.velocityEngine = velocityEngine;
    }

    @Transactional
    @Override
    public RegistrationCodeSentEvent sendRegistrationCode(SendRegisterCodeCommand command) {
        final EmailRegistrationCodeEntity entity = EmailRegistrationCodeEntity
                .newCode(command.getEmail());

        final EmailRegistrationCodeEntity savedEntity = emailRegistrationCodeRepository
                .save(entity);
        sendRegistrationCodeTo(command.getEmail(), entity.getCode());

        return new RegistrationCodeSentEvent(savedEntity.getEmail());
    }

    @Override
    public Optional<EmailRegistrationCodeEntity> findFreshRegistrationCodeByEmailAndCode(EmailValue email,
                                                                                         String code) {

        return emailRegistrationCodeRepository.findByEmailAndCode(email.getValue(), code)
                .stream()
                .filter(it -> !it.getIsConfirmed())
                .filter(it -> Utils.isRegistrationCodeFresh(it.getCreatedTime()))
                .findAny();
    }

    @Override
    public UserEmailRegistrationCodeConfirmedEvent confirmEmailCode(ConfirmRegistrationCodeCommand command) {
        final UUID registrationCodeId = command.getRegistrationCodeEntityId();
        final EmailRegistrationCodeEntity entity = emailRegistrationCodeRepository.findById(registrationCodeId)
                .orElseThrow(() -> new RegistrationCodeNotFoundException("Failed to find registration code with id: " + registrationCodeId));

        entity.setIsConfirmed(true);

        emailRegistrationCodeRepository.save(entity);
        return new UserEmailRegistrationCodeConfirmedEvent(
                entity.getId(),
                entity.getEmail(),
                entity.getCode()
        );
    }

    @Override
    public Optional<EmailRegistrationCodeEntity> findFreshRegistrationCodeByEmail(EmailValue email) {
        return emailRegistrationCodeRepository.findByEmail(email.getValue())
                .stream()
                .filter(it -> Utils.isRegistrationCodeFresh(it.getCreatedTime()))
                .findAny();
    }

    private void sendRegistrationCodeTo(EmailValue email, String code) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

            message.setSubject(EMAIL_SUBJECT_TEXT);
            message.setFrom(APP_EMAIL_ADDRESS); // could be parameterized...
            message.setTo(email.getValue());

            final Template template = velocityEngine.getTemplate(REGISTRATION_CODE_EMAIL_TEMPLATE);

            final VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("code", code);

            final StringWriter writer = new StringWriter();
            template.merge(velocityContext, writer);
            message.setText(writer.toString(), true);
        };
        this.mailSender.send(preparator);
    }

    public static class Utils {
        public static boolean isRegistrationCodeFresh(DateTime createdTime) {
            final int minutes = Minutes.minutesBetween(createdTime, UTime.timeNow())
                    .getMinutes();
            return minutes < CODE_EXPIRATION_MINUTES;
        }

    }
}
