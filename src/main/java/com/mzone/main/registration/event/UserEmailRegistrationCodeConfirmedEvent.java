package com.mzone.main.registration.event;

import lombok.Data;

import java.util.UUID;

@Data
public class UserEmailRegistrationCodeConfirmedEvent {
    private final UUID id;
    private final String email;
    private final String code;
}
