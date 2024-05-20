package com.mzone.main.media.service;

import com.mzone.main.core.exception.FailedToRemoveTempImage;
import com.mzone.main.core.exception.ImageSavingException;
import com.mzone.main.media.UFile;
import com.mzone.main.media.entity.FileEntity;
import com.mzone.main.media.entity.ImageEntity;
import com.mzone.main.media.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Component
public class DefaultMediaService implements MediaService {

    private final ImageRepository imageRepository;
    private final MediaContentStorage fileManageMediaStorage;
    private final MediaContentStorage jpaMediaStorage;

    public DefaultMediaService(ImageRepository imageRepository,
                               @Qualifier("fileManageMediaStorage")
                               MediaContentStorage fileManageMediaStorage,
                               @Qualifier("jpaMediaStorage")
                               MediaContentStorage jpaMediaStorage) {
        this.imageRepository = imageRepository;
        this.fileManageMediaStorage = fileManageMediaStorage;
        this.jpaMediaStorage = jpaMediaStorage;
    }

    @Override
    public ImageEntity store(MultipartFile image) {
        FileEntity fileEntity = UFile.readAllFile(image);
        try {
            fileEntity = jpaMediaStorage.storeImage(fileEntity);
            final ImageEntity imageEntity = Optional.of(fileEntity)
                    .map(it -> new ImageEntity(
                            it.getId(),
                            it.getNamePrefix(),
                            it.getFullName(),
                            it.getNameSuffix()
                    )).get();
            final ImageEntity savedImage = imageRepository.save(imageEntity);
            fileEntity = fileManageMediaStorage.storeImage(fileEntity);

            return savedImage;
        } catch (Exception e) {
            final String imageOriginalName = String.valueOf(image.getOriginalFilename());
            Optional.ofNullable(fileEntity)
                    .ifPresent(it -> {
                        try {
                            jpaMediaStorage.removeImage(it);
                        } catch (Exception e2) {
                            e2.addSuppressed(e);
                            throw new FailedToRemoveTempImage(imageOriginalName, it, e2);
                        }
                    });
            throw new ImageSavingException(imageOriginalName, e);
        }
    }

    @Override
    public byte[] readFileData(UUID fileId) {
        return fileManageMediaStorage.findFile(fileId)
                .or(() -> jpaMediaStorage.findFile(fileId))
                .map(FileEntity::getData)
                .orElseThrow();
    }

}
