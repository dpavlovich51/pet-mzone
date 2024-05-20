package com.mzone.main.events.repository;

import com.mzone.main.events.MediaEventEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MediaEventRepository extends CrudRepository<MediaEventEntity, UUID> {

}
