package ru.cohenrol.profile.domain.exception.inner;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public sealed abstract class ImageException
    extends RuntimeException
    permits ImageEmptyException, ImageUploadProcessException
{
    @Getter
    final private HttpStatusCode statusCode;
//    @Getter
//    final private String serviceName;

    public ImageException(String cause, HttpStatusCode statusCode) {
        super(cause);
        this.statusCode = statusCode;
//        this.serviceName = serviceName;
    }
}
