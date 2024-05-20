package com.mzone.main.media.service;

import com.mzone.main.media.entity.FileEntity;
import com.mzone.main.media.repository.FileRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Qualifier("jpaMediaStorage")
@Component
public class JpaMediaContentStorage implements MediaContentStorage {

    private final FileRepository fileRepository;

    public JpaMediaContentStorage(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public Optional<FileEntity> findFile(UUID id) {
        return fileRepository.findById(id);
    }

    @Override
    public FileEntity storeImage(FileEntity file) {
        return fileRepository.save(file);
    }

    @Override
    public void removeImage(FileEntity file) {
        fileRepository.deleteById(file.getId());
    }

}
