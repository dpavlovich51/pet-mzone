package com.mzone.main.media.service;

import com.mzone.main.media.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MediaService {

    ImageEntity store(MultipartFile image);

    byte[] readFileData(UUID imageName);

}
