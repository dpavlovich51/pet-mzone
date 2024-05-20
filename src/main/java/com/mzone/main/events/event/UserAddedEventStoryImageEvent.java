package com.mzone.main.events.event;

import com.mzone.main.media.entity.ImageEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class UserAddedEventStoryImageEvent {

    private final UUID eventId;
    private final ImageEntity image;

    public static UserAddedEventStoryImageEvent create(UUID eventId,
                                                       ImageEntity image) {
        return new UserAddedEventStoryImageEvent(eventId, image);
    }
}
