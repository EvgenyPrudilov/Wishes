package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class ItemNotFoundException extends ItemException {
    public ItemNotFoundException() {
        super("Item does not belong to this wishlist.", HttpStatus.NOT_FOUND);
    }
}