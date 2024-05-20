package com.mzone.main.events.event;

import com.mzone.main.events.value.LocationValue;
import com.mzone.main.user.entity.UserEntity;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Set;

@Data
public class CreateEventCommand {

    private final UserEntity user;
    private final String name;
    private final String description;
    private final Set<EventCategory> categories;
    private final Integer availability;
    private final LocationValue locationValue;
    private final DateTime startDate;
    private final DateTime endDate;

    public static CreateEventCommand create(UserCreatedNewEventEvent event) {
        return new CreateEventCommand(
                event.getUser(),
                event.getName(),
                event.getDescription(),
                event.getCategories(),
                event.getAvailability(),
                event.getLocationValue(),
                event.getStartTime(),
                event.getEndTime()
        );
    }

}
