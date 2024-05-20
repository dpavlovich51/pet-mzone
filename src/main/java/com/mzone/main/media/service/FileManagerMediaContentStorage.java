package com.mzone.main.media.service;

import com.mzone.main.events.exception.ImageUploadException;
import com.mzone.main.media.entity.FileEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

@Qualifier("fileManageMediaStorage")
@Component
public class FileManagerMediaContentStorage implements MediaContentStorage {

    public static final Path MEDIAL_FOLDER_PATH = Paths.get("uploads");

    @PostConstruct
    private void init() {
        if (!MEDIAL_FOLDER_PATH.toFile().exists()) {
            try {
                java.nio.file.Files.createDirectory(MEDIAL_FOLDER_PATH);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create upload folder", e);
            }
        }
    }

    @Override
    public Optional<FileEntity> findFile(UUID id) {
        // TODO: 08/06/2022 IMPL
        return Optional.empty();
    }

    @Override
    public FileEntity storeImage(FileEntity fileData) {
        //check if size more than ... 3MB
        //check extension png

        try {
            final Path path = MEDIAL_FOLDER_PATH.resolve(fileData.getFullName());
            Files.write(path,
                    fileData.getData(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);

            return fileData;
        } catch (IOException e) {
            throw new ImageUploadException(fileData.toString(), e);
        }
    }

    @Override
    public void removeImage(FileEntity ref) {
        //// TODO: 03/06/2022 implement it
    }

}
