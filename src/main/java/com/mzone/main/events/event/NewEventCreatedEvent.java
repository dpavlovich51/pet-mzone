package com.mzone.main.events.event;

import com.mzone.main.core.DomainEvent;
import com.mzone.main.events.EventEntity;
import com.mzone.main.events.data.Event;
import lombok.Data;

@Data
public class NewEventCreatedEvent extends DomainEvent {

    private Event event;

    public NewEventCreatedEvent(Event event) {
        this.event = event;
    }

    public static NewEventCreatedEvent create(EventEntity saved) {
        return new NewEventCreatedEvent(Event.createFromEntity(saved));
    }

}
