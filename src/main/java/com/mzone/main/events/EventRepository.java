package com.mzone.main.events;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface EventRepository extends CrudRepository<EventEntity, UUID> {

    Collection<EventEntity> findEventsByGeoHashIn(Collection<String> geoHashes);

}
