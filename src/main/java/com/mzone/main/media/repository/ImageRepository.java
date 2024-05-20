package com.mzone.main.media.repository;

import com.mzone.main.media.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ImageRepository extends CrudRepository<ImageEntity, UUID> {
}
