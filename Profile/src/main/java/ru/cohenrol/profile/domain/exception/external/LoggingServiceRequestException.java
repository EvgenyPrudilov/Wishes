package ru.cohenrol.profile.domain.exception.external;

import org.springframework.http.HttpStatusCode;

public final class LoggingServiceRequestException extends ExternalServiceRequestException {
    public LoggingServiceRequestException(HttpStatusCode statusCode) {
        super("Logging service error.", statusCode, "logging-service");
    }
}