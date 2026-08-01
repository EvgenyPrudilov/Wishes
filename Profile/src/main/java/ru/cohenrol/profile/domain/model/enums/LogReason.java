package ru.cohenrol.profile.domain.model.enums;

import ru.cohenrol.profile.domain.exception.external.ExternalServiceRequestException;
import ru.cohenrol.profile.domain.exception.external.LoggingServiceRequestException;
import ru.cohenrol.profile.domain.exception.inner.RelationAlreadyExistsException;
import ru.cohenrol.profile.domain.exception.inner.ProfileException;
import ru.cohenrol.profile.domain.exception.inner.UserNotFoundException;

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

    public static LogReason getLogReason(ProfileException ex) {
        return switch (ex) {
//            case ProfileAlreadyCancelledException e -> INTERNAL_ENROLLMENT_ALREADY_CANCELLED_ERROR;
            case RelationAlreadyExistsException e -> INTERNAL_ENROLLMENT_ALREADY_EXISTS_ERROR;
            case UserNotFoundException e -> INTERNAL_ENROLLMENT_NOT_FOUND_ERROR;
            default -> throw new IllegalStateException("Unexpected value: " + ex);
        };
    }
}
