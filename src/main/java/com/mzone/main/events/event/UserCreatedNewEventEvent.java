package com.mzone.main.events.event;

import com.mzone.main.core.DomainEvent;
import com.mzone.main.events.value.LocationValue;
import com.mzone.main.user.entity.UserEntity;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Set;

@Data
public class UserCreatedNewEventEvent extends DomainEvent {
    private final UserEntity user;
    private final String name;
    private final String description;
    private final Set<EventCategory> categories;
    private final Integer availability;
    private final LocationValue locationValue;
    private final DateTime startTime;
    private final DateTime endTime;

    private UserCreatedNewEventEvent(UserEntity user,
                                     String name,
                                     String description,
                                     Set<EventCategory> categories,
                                     Integer availability,
                                     LocationValue locationValue,
                                     DateTime startTime,
                                     DateTime endTime) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.availability = availability;
        this.locationValue = locationValue;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static UserCreatedNewEventEvent create(UserEntity user,
                                                  String name,
                                                  String description,
                                                  Set<EventCategory> categories,
                                                  Integer availability,
                                                  LocationValue locationValue,
                                                  DateTime startTime,
                                                  DateTime endTime) {
        return new UserCreatedNewEventEvent(
                user,
                name,
                description,
                categories,
                availability,
                locationValue,
                startTime,
                endTime);
    }
}
