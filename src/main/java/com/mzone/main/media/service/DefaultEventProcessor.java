package com.mzone.main.media.service;

import com.mzone.main.events.MediaEventEntity;
import com.mzone.main.events.event.AddTitleImageToEventCommand;
import com.mzone.main.events.event.EventTitleImageChangedEvent;
import com.mzone.main.events.repository.MediaEventRepository;
import com.mzone.main.media.entity.ImageEntity;
import com.mzone.main.media.event.AddStoryImageToEventCommand;
import com.mzone.main.media.event.EventStoryImageAddedEvent;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DefaultEventProcessor implements EventProcessor {
    private final MediaEventRepository mediaEventRepository;

    public DefaultEventProcessor(MediaEventRepository mediaEventRepository) {
        this.mediaEventRepository = mediaEventRepository;
    }

    @Override
    public EventTitleImageChangedEvent changeTitleImage(AddTitleImageToEventCommand command) {
        final ImageEntity image = command.getImage();

        final MediaEventEntity mediaEventEntity = mediaEventRepository.findById(command.getEventId())
                .orElse(MediaEventEntity.create(command.getEventId()));

        mediaEventEntity.setTitleImage(image);

        final MediaEventEntity saved = mediaEventRepository.save(mediaEventEntity);
        return new EventTitleImageChangedEvent(saved.getEvent().getId(), image);
    }

    @Override
    public EventStoryImageAddedEvent addStoryImage(AddStoryImageToEventCommand command) {
        final ImageEntity image = command.getImage();

        final MediaEventEntity mediaEventEntity = mediaEventRepository.findById(command.getEventId())
                .orElse(MediaEventEntity.create(command.getEventId()));

        mediaEventEntity.getStoryImages().add(image);

        final MediaEventEntity saved = mediaEventRepository.save(mediaEventEntity);
        return new EventStoryImageAddedEvent(saved.getEvent().getId(), image);
    }

}
