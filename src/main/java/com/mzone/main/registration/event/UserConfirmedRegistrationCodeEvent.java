package com.mzone.main.registration.event;

import com.mzone.main.registration.entity.EmailValue;
import lombok.Data;

@Data
public class UserConfirmedRegistrationCodeEvent {

    private final EmailValue email;
    private final String code;

}
