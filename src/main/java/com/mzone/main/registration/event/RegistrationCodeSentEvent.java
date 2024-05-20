package com.mzone.main.registration.event;

import lombok.Data;

@Data
public class RegistrationCodeSentEvent {

    private final String email;

    public RegistrationCodeSentEvent(String email) {
        this.email = email;
    }
}