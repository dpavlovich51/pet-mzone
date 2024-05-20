package com.mzone.main.media.service;

import com.mzone.main.events.event.AddTitleImageToEventCommand;
import com.mzone.main.events.event.EventTitleImageChangedEvent;
import com.mzone.main.media.event.AddStoryImageToEventCommand;
import com.mzone.main.media.event.EventStoryImageAddedEvent;

public interface EventProcessor {

    EventStoryImageAddedEvent addStoryImage(AddStoryImageToEventCommand command);

    EventTitleImageChangedEvent changeTitleImage(AddTitleImageToEventCommand command);

}
