package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public final class UserNotFoundException extends ProfileException {
    public UserNotFoundException(UUID user) {
        super("User [" + user + "] not found.", HttpStatus.NOT_FOUND);
    }
}