package com.mzone.main.registration.event;

import lombok.Data;

@Data
public class NewAccountSuccessfullyRegisteredEvent {

    private final String token;
    private final String refreshToken;

}
