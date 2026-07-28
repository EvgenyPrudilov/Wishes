package ru.cohenrol.profile.domain.exception.external;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public sealed abstract class ExternalServiceRequestException
    extends RuntimeException
    permits LoggingServiceRequestException {
    @Getter
    final private HttpStatusCode statusCode;
    @Getter
    final private String serviceName;

    public ExternalServiceRequestException(String cause, HttpStatusCode statusCode, String serviceName) {
        super(cause);
        this.statusCode = statusCode;
        this.serviceName = serviceName;
    }
}
