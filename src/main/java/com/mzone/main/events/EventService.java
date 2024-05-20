package com.mzone.main.events;

import com.mzone.main.events.event.CreateEventCommand;
import com.mzone.main.events.event.NewEventCreatedEvent;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface EventService {

    NewEventCreatedEvent create(CreateEventCommand event);

    Collection<MediaEventEntity> retrieve(FindEventModel toFindEventModel);

    Optional<MediaEventEntity> retrieveEventWithMedia(UUID eventId);

    EventEntity retrieve(UUID eventId);

}
