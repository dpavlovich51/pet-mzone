package com.mzone.main.registration.entity;

import lombok.Data;

@Data
public class EmailValue {
    private final String value;

    private EmailValue(String email) {
        this.value = email;
    }

    /**
     * Email is created always with lower case!
     */
    public static EmailValue create(String email) {
        return new EmailValue(email.toLowerCase());
    }

    @Override
    public String toString() {
        return value;
    }
}
