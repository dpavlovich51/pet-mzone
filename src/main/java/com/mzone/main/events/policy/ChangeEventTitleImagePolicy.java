package com.mzone.main.events.policy;

import com.mzone.main.events.event.AddTitleImageToEventCommand;
import com.mzone.main.events.event.EventTitleImageChangedEvent;
import com.mzone.main.events.event.UserChangedEventTitleImageEvent;
import com.mzone.main.media.service.EventProcessor;
import com.mzone.main.registration.policy.BusinessPolicyExecutor;
import org.springframework.stereotype.Component;

@Component
public class ChangeEventTitleImagePolicy implements BusinessPolicyExecutor
        <EventTitleImageChangedEvent, UserChangedEventTitleImageEvent> {

    private final EventProcessor eventProcessor;

    public ChangeEventTitleImagePolicy(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @Override
    public EventTitleImageChangedEvent tryExecute(UserChangedEventTitleImageEvent event) {
        //// TODO: 07/06/2022 add policy logic
        return eventProcessor.changeTitleImage(new AddTitleImageToEventCommand(
                event.getEventId(),
                event.getTitleImage()
        ));
    }
}
