package ru.cohenrol.profile.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cohenrol.profile.domain.client.LoggingServiceClient;
import ru.cohenrol.profile.domain.exception.inner.ProfileException;
import ru.cohenrol.profile.domain.model.enums.LogLevel;
import ru.cohenrol.profile.domain.model.enums.LogReason;

import java.time.Instant;

@RequiredArgsConstructor

@RestControllerAdvice
public class InnerExceptionHandler {
    private final LoggingServiceClient loggingServiceClient;

    @ExceptionHandler(ProfileException.class)
    public ProblemDetail handleExternalServiceError(ProfileException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            ex.getStatusCode(),
            ex.getMessage()
        );
        problem.setProperty("timestamp", Instant.now());

        loggingServiceClient.logEvent(LogLevel.ERROR, LogReason.getLogReason(ex), ex.getMessage());
        return problem;
    }
}
