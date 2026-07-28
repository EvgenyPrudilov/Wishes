package ru.cohenrol.profile.domain.model.enums;

import ru.cohenrol.profile.domain.exception.external.ExternalServiceRequestException;
import ru.cohenrol.profile.domain.exception.external.LoggingServiceRequestException;
import ru.cohenrol.profile.domain.exception.inner.EnrollmentAlreadyCancelledException;
import ru.cohenrol.profile.domain.exception.inner.EnrollmentAlreadyExistsException;
import ru.cohenrol.profile.domain.exception.inner.EnrollmentException;
import ru.cohenrol.profile.domain.exception.inner.EnrollmentNotFoundException;

public enum LogReason {
    COURSE_ENROLLMENT, COURSE_CANCELLATION,

    EXTERNAL_PROGRESS_SERVICE_ERROR,
    EXTERNAL_COURSES_SERVICE_ERROR,
    EXTERNAL_LOGGING_SERVICE_ERROR,

    INTERNAL_ENROLLMENT_ALREADY_CANCELLED_ERROR,
    INTERNAL_ENROLLMENT_ALREADY_EXISTS_ERROR,
    INTERNAL_ENROLLMENT_NOT_FOUND_ERROR;

    public static LogReason getLogReason(ExternalServiceRequestException ex) {
        return switch (ex) {
            case LoggingServiceRequestException e -> EXTERNAL_LOGGING_SERVICE_ERROR;
        };
    }

    public static LogReason getLogReason(EnrollmentException ex) {
        return switch (ex) {
            case EnrollmentAlreadyCancelledException e -> INTERNAL_ENROLLMENT_ALREADY_CANCELLED_ERROR;
            case EnrollmentAlreadyExistsException e -> INTERNAL_ENROLLMENT_ALREADY_EXISTS_ERROR;
            case EnrollmentNotFoundException e -> INTERNAL_ENROLLMENT_NOT_FOUND_ERROR;
        };
    }
}
