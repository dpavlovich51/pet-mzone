package com.mzone.main.events.policy;

import com.mzone.main.events.event.UserAddedEventStoryImageEvent;
import com.mzone.main.media.event.AddStoryImageToEventCommand;
import com.mzone.main.media.event.EventStoryImageAddedEvent;
import com.mzone.main.media.service.EventProcessor;
import com.mzone.main.registration.policy.BusinessPolicyExecutor;
import org.springframework.stereotype.Component;

@Component
public class AddStoryImageToEventPolicy implements BusinessPolicyExecutor<EventStoryImageAddedEvent, UserAddedEventStoryImageEvent> {
    private final EventProcessor eventProcessor;

    public AddStoryImageToEventPolicy(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @Override
    public EventStoryImageAddedEvent tryExecute(UserAddedEventStoryImageEvent event) {
        return eventProcessor.addStoryImage(AddStoryImageToEventCommand.create(
                event.getEventId(),
                event.getImage()
        ));
    }
}
