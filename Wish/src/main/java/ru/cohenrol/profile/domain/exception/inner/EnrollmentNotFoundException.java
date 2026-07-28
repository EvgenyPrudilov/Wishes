package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;

public final class EnrollmentNotFoundException extends EnrollmentException {
    public EnrollmentNotFoundException() {
        super("Enrollment not found for user.", HttpStatus.NOT_FOUND);
    }
}