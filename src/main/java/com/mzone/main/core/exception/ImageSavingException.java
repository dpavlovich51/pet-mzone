package com.mzone.main.core.exception;

public class ImageSavingException extends RuntimeException {
    public ImageSavingException(String imageName, Exception e) {
        super("Failed to save image. Image original name: " + imageName, e);
    }
}
