package com.mzone.main.events.event;

import com.mzone.main.core.DomainEvent;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.UUID;

@Data
public class CreateNewEventCommand extends DomainEvent {

    private final UUID eventId;
    private final MultipartFile titleImage;
    private final Collection<MultipartFile> images;

}
