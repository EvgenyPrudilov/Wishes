package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;
import ru.cohenrol.profile.datasource.model.WishlistEntity;

public final class EnrollmentAlreadyExistsException extends EnrollmentException {
    public EnrollmentAlreadyExistsException() {
        super("User is already enrolled in this course.", HttpStatus.BAD_REQUEST);
    }

    public EnrollmentAlreadyExistsException(WishlistEntity wishlistEntity) {
        this();
    }
}
