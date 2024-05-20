package com.mzone.main.media.service;

import com.mzone.main.media.entity.FileEntity;

import java.util.Optional;
import java.util.UUID;

public interface MediaContentStorage {

    Optional<FileEntity> findFile(UUID id);

    FileEntity storeImage(FileEntity file);

    void removeImage(FileEntity image);

}
