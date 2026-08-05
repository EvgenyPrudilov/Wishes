package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class NotWishlistOwnerException extends WishlistException {
    public NotWishlistOwnerException() {
        super("User is not the owner of the wishlist.", HttpStatus.BAD_REQUEST);
    }

//    public NotOwnerException(WishlistEntity wishlistEntity) {
//        this();
//    }
}
