package ru.cohenrol.profile.web.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cohenrol.profile.domain.client.LoggingServiceClient;
import ru.cohenrol.profile.domain.exception.external.ExternalServiceRequestException;
import ru.cohenrol.profile.domain.model.enums.LogLevel;
import ru.cohenrol.profile.domain.model.enums.LogReason;

import java.time.Instant;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExternalExceptionHandler {
    private final LoggingServiceClient loggingServiceClient;

    @ExceptionHandler(ExternalServiceRequestException.class)
    public ProblemDetail handleExternalServiceError(ExternalServiceRequestException ex) {
        HttpStatusCode externalStatus = ex.getStatusCode();
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_GATEWAY,
            ex.getMessage()
        );
        problem.setProperty("external_status_code", externalStatus.value());
        problem.setProperty("timestamp", Instant.now());

        loggingServiceClient.logEvent(LogLevel.ERROR, LogReason.getLogReason(ex), ex.getMessage());
        return problem;
    }
}
