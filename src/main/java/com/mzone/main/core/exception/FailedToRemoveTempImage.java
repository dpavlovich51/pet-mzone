package com.mzone.main.core.exception;

import com.mzone.main.media.entity.FileEntity;

public class FailedToRemoveTempImage extends RuntimeException {
    public FailedToRemoveTempImage(String originalName,
                                   FileEntity imageRef,
                                   Exception ex) {
        super("Failed to remove image: " + imageRef + "; original name: " + originalName, ex);
    }
}
