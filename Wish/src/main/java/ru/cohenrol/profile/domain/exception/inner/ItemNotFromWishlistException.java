package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class ItemNotFromWishlistException extends ItemException {
    public ItemNotFromWishlistException() {
        super("Item not found.", HttpStatus.BAD_REQUEST);
    }
}