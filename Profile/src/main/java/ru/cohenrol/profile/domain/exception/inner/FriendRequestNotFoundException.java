package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public final class FriendRequestNotFoundException extends ProfileException {
    public FriendRequestNotFoundException(UUID user) {
        super("Friend request from [" + user + "] not found.", HttpStatus.NOT_FOUND);
    }
}