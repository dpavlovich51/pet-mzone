package com.mzone.main.user.model;

import com.mzone.main.user.entity.UserEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class UserModel {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final Integer followerCount;
    private final UUID avatar;

    private UserModel(UUID id,
                     String firstName,
                     String lastName,
                     Integer followerCount,
                     UUID avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.followerCount = followerCount;
        this.avatar = avatar;
    }

    public static UserModel create(UserEntity user,
                                   Integer followerCount,
                                   UUID avatarId) {
        return new UserModel(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                followerCount,
                avatarId
        );
    }
}