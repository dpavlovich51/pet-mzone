package com.mzone.main.events.policy;

import com.mzone.main.events.EventService;
import com.mzone.main.events.event.CreateEventCommand;
import com.mzone.main.events.event.NewEventCreatedEvent;
import com.mzone.main.events.event.UserCreatedNewEventEvent;
import com.mzone.main.registration.policy.BusinessPolicyExecutor;
import org.springframework.stereotype.Component;

@Component
public class EventCreatingPolicy implements BusinessPolicyExecutor<NewEventCreatedEvent, UserCreatedNewEventEvent> {

    private final EventService eventService;

    public EventCreatingPolicy(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public NewEventCreatedEvent tryExecute(UserCreatedNewEventEvent event) {
        // check if user can create event
        // check if user has available count to create event

        return eventService.create(CreateEventCommand.create(event));
    }

}
