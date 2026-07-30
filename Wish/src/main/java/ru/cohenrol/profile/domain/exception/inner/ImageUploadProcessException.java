package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class ImageUploadProcessException extends ImageException {
    public ImageUploadProcessException() {
        super("Internal error while an image processing.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
