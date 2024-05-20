package com.mzone.main.core;

import com.mzone.main.media.service.MediaService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping(
            value = "/{imageName}",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] getImage(@PathVariable UUID imageName) {
        return mediaService.readFileData(imageName);
    }

}
