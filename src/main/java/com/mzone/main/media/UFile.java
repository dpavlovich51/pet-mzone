package com.mzone.main.media;

import com.google.common.io.Files;
import com.mzone.main.core.UTime;
import com.mzone.main.media.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class UFile {

    public static FileEntity readAllFile(MultipartFile file) {
        final String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow();

        final UUID imageId = UUID.randomUUID();
        final String namePrefix = imageId.toString();
        final String nameSuffix = Files.getFileExtension(originalName);

        try {
            return new FileEntity(imageId,
                    namePrefix,
                    nameSuffix,
                    originalName,
                    file.getBytes(),
                    UTime.timeNow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
