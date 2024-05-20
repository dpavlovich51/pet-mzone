package com.mzone.main.events;

import ch.hsr.geohash.GeoHash;
import com.mzone.main.events.event.CreateEventCommand;
import com.mzone.main.events.event.NewEventCreatedEvent;
import com.mzone.main.events.repository.MediaEventRepository;
import com.mzone.main.events.value.LocationValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DefaultEventService implements EventService {

    private final EventRepository eventRepository;
    private final MediaEventRepository mediaEventRepository;

    public DefaultEventService(EventRepository eventRepository,
                               MediaEventRepository mediaEventRepository) {
        this.eventRepository = eventRepository;
        this.mediaEventRepository = mediaEventRepository;
    }

    @Override
    public NewEventCreatedEvent create(CreateEventCommand command) {
        final LocationValue location = command.getLocationValue();

        final EventEntity eventEntity = EventEntity.create(
                command.getName(),
                command.getCategories(),
                command.getAvailability(),
                command.getDescription(),
                location.getLat(),
                location.getLng(),
                location.toGeoHash(),
                command.getUser(),
                command.getStartDate(),
                command.getEndDate()
        );

        final EventEntity saved = eventRepository.save(eventEntity);
        return NewEventCreatedEvent.create(saved);
    }

    @Override
    public Collection<MediaEventEntity> retrieve(FindEventModel model) {
        final GeoHash geoHash = GeoHash.withCharacterPrecision(
                model.getLatitude().doubleValue(),
                model.getLongitude().doubleValue(),
                5
        );

        final ArrayList<String> geoHashes = new ArrayList<>();
        geoHashes.add(geoHash.toBase32().substring(0, 5));
        for (GeoHash hash : geoHash.getAdjacent()) {
            geoHashes.add(hash.toBase32().substring(0, 5));
        }

        //// TODO: 06/07/2022 use criteria builder  !!!
        return eventRepository.findEventsByGeoHashIn(geoHashes)
                .stream()
                .map(eventEntity -> mediaEventRepository.findById(eventEntity.getId())
                        .orElse(MediaEventEntity.create(eventEntity, null)))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<MediaEventEntity> retrieveEventWithMedia(UUID eventId) {
        return mediaEventRepository.findById(eventId);
    }

    @Override
    public EventEntity retrieve(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow();
    }

}
