package com.mzone.main.media.event;

import com.mzone.main.core.DomainCommand;
import com.mzone.main.media.entity.ImageEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class AddStoryImageToEventCommand extends DomainCommand {

    private final ImageEntity image;
    private UUID eventId;

    private AddStoryImageToEventCommand(UUID eventId, ImageEntity image) {
        this.eventId = eventId;
        this.image = image;
    }

    public static AddStoryImageToEventCommand create(UUID eventId, ImageEntity image) {
        return new AddStoryImageToEventCommand(eventId, image);
    }

}
