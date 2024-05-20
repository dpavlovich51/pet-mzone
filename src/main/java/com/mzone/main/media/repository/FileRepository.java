package com.mzone.main.media.repository;

import com.mzone.main.media.entity.FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FileRepository extends CrudRepository<FileEntity, UUID> {
}
