package com.mzone.main.registration.command;

import com.mzone.main.core.Gender;
import com.mzone.main.entity.UserRole;
import com.mzone.main.registration.entity.EmailValue;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class CreateNewAccountCommand {

    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final DateTime birthday;
    private final EmailValue email;
    private final String password;
    private final Set<UserRole> roles;

}
