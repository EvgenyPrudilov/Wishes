package ru.cohenrol.profile.domain.exception.inner;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public sealed abstract class EnrollmentException
    extends RuntimeException
    permits EnrollmentAlreadyExistsException, EnrollmentNotFoundException, EnrollmentAlreadyCancelledException
{
    @Getter
    final private HttpStatusCode statusCode;
//    @Getter
//    final private String serviceName;

    public EnrollmentException(String cause, HttpStatusCode statusCode) {
        super(cause);
        this.statusCode = statusCode;
//        this.serviceName = serviceName;
    }
}
