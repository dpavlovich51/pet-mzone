package com.mzone.main.events.event;

import com.mzone.main.media.entity.ImageEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class AddTitleImageToEventCommand {
    private final UUID eventId;
    private final ImageEntity image;
}
