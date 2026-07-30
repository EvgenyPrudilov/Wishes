package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public final class ImageEmptyException extends ImageException {
    public ImageEmptyException() {
        super("Image must not be empty.", HttpStatus.BAD_REQUEST);
    }
}
