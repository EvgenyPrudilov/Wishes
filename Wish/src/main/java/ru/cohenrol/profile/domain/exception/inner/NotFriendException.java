package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class NotFriendException extends WishlistException {
    public NotFriendException() {
        super("User is not a friend.", HttpStatus.BAD_REQUEST);
    }

//    public NotOwnerException(WishlistEntity wishlistEntity) {
//        this();
//    }
}
