package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class AlreadyFriendsException extends ProfileException {
    public AlreadyFriendsException() {
        super("You are already friends.", HttpStatus.BAD_REQUEST);
    }
}