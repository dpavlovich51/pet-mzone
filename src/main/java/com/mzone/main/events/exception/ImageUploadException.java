package com.mzone.main.events.exception;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String message) {
        super(message);
    }

    public ImageUploadException(String name, Exception e) {
        super(name, e);
    }
}
