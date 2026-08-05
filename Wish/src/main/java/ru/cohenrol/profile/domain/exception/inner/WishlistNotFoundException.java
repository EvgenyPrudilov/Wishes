package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class WishlistNotFoundException extends WishlistException {
    public WishlistNotFoundException() {
        super("Wishlist not found.", HttpStatus.NOT_FOUND);
    }
}