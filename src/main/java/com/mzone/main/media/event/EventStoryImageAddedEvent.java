package com.mzone.main.media.event;

import com.mzone.main.core.DomainEvent;
import com.mzone.main.media.entity.ImageEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class EventStoryImageAddedEvent extends DomainEvent {

    private final UUID eventId;
    private final ImageEntity storyImage;

}
