package ru.cohenrol.profile.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cohenrol.profile.client.LoggingServiceClient;
import ru.cohenrol.profile.domain.exception.inner.WishlistException;
import ru.cohenrol.profile.domain.model.enums.LogLevel;
import ru.cohenrol.profile.domain.model.enums.LogReason;

import java.time.Instant;

@RequiredArgsConstructor

@RestControllerAdvice
public class InnerExceptionHandler {
    private final LoggingServiceClient loggingServiceClient;

    @ExceptionHandler(WishlistException.class)
    public ProblemDetail handleExternalServiceError(WishlistException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            ex.getStatusCode(),
            ex.getMessage()
        );
        problem.setProperty("timestamp", Instant.now());

        loggingServiceClient.logEvent(LogLevel.ERROR, LogReason.getLogReason(ex), ex.getMessage());
        return problem;
    }
}
