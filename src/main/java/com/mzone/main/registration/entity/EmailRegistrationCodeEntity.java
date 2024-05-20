package com.mzone.main.registration.entity;

import com.mzone.main.core.BaseEntity;
import com.mzone.main.core.UTime;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@Entity
@Table(name = "email_registration_codes")
public class EmailRegistrationCodeEntity extends BaseEntity {

    public static final int CODE_LENGTH = 6;

    @Column(name = "email")
    private String email;
    @Column(name = "code")
    private String code;
    @Column(name = "created_time")
    private DateTime createdTime;
    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    public EmailRegistrationCodeEntity() {
    }

    private EmailRegistrationCodeEntity(UUID id,
                                        EmailValue email,
                                        String code,
                                        DateTime createdTime,
                                        boolean isConfirmed) {
        super(id);
        this.email = email.getValue();
        this.code = code;
        this.createdTime = createdTime;
        this.isConfirmed = isConfirmed;
    }

    public static EmailRegistrationCodeEntity newCode(EmailValue email) {
        return new EmailRegistrationCodeEntity(
                UUID.randomUUID(),
                email,
                generateCode(),
                UTime.timeNow(),
                false);
    }

    private static String generateCode() {
        final String doubleNumber = String.valueOf(ThreadLocalRandom.current().nextDouble());
        return doubleNumber.substring(doubleNumber.length() - CODE_LENGTH);
    }

}
