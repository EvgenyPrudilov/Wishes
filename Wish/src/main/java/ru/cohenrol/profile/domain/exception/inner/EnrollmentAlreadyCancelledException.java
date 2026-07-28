package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class EnrollmentAlreadyCancelledException extends EnrollmentException {
    public EnrollmentAlreadyCancelledException() {
        super("Enrollment is already cancelled.", HttpStatus.BAD_REQUEST);
    }
}
