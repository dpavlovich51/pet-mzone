package com.mzone.main.registration.event;

import com.mzone.main.registration.entity.EmailValue;
import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserSingUpEvent {
    private final String firstName;
    private final String lastName;
    private final Integer gender;
    private final DateTime birthday;
    private final EmailValue email;
    private final String password;
}
