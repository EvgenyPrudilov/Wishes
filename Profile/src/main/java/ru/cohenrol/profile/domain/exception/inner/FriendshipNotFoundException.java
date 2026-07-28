package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public final class FriendshipNotFoundException extends ProfileException {
    public FriendshipNotFoundException(UUID user1, UUID user2) {
        super("Friendship between [" + user1 + "] and [" + user2 + "] not found.", HttpStatus.NOT_FOUND);
    }
}