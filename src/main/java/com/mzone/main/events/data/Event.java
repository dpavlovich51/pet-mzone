package com.mzone.main.events.data;

import com.mzone.main.events.EventEntity;
import com.mzone.main.events.event.EventCategory;
import com.mzone.main.events.value.LocationValue;
import com.mzone.main.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor
public class Event {

    private final UUID id;
    private final String name;
    private final Set<EventCategory> categories;
    private final Integer availability;
    private final String description;
    private final LocationValue location;
    private final UserEntity owner;
    private final DateTime startDate;
    private final DateTime endDate;
    private final DateTime createdAt;
    private final DateTime updatedAt;

    public static Event createFromEntity(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getName(),
                entity.getCategories(),
                entity.getAvailability(),
                entity.getDescription(),
                LocationValue.create(entity.getLatitude(), entity.getLongitude()),
                entity.getUser(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
