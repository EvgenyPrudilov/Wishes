package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class ItemNotReservedException extends ItemException {
    public ItemNotReservedException() {
        super("Item is not reserved.", HttpStatus.BAD_REQUEST);
    }
}