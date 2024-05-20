package com.mzone.main.registration.command;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmRegistrationCodeCommand {
    private final UUID registrationCodeEntityId;
}
