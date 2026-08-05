package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class ItemAlreadyReservedException extends ItemException {
    public ItemAlreadyReservedException() {
        super("Item is already reserved.", HttpStatus.BAD_REQUEST);
    }
}