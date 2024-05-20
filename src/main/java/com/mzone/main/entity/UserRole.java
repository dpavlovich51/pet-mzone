package com.mzone.main.entity;

import org.springframework.security.core.*;

public enum UserRole implements GrantedAuthority {

    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
