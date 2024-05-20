package com.mzone.main.events.event;

import com.mzone.main.core.DomainEvent;
import com.mzone.main.media.entity.ImageEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class UserChangedEventTitleImageEvent extends DomainEvent {

    private final UUID eventId;
    private final ImageEntity titleImage;

    public static UserChangedEventTitleImageEvent create(UUID eventId, ImageEntity titleImage) {
        return new UserChangedEventTitleImageEvent(eventId, titleImage);
    }
}
