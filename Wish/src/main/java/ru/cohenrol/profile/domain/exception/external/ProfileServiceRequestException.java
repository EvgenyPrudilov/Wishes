package ru.cohenrol.profile.domain.exception.external;

import org.springframework.http.HttpStatusCode;

public final class ProfileServiceRequestException extends ExternalServiceRequestException {
    public ProfileServiceRequestException(HttpStatusCode statusCode) {
        super("Profile service error.", statusCode, "profile-service");
    }
}