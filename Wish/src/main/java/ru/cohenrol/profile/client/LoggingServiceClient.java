package ru.cohenrol.profile.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.cohenrol.profile.domain.exception.external.LoggingServiceRequestException;
import ru.cohenrol.profile.domain.model.Log;
import ru.cohenrol.profile.domain.model.enums.LogLevel;
import ru.cohenrol.profile.domain.model.enums.LogReason;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Component
public class LoggingServiceClient {
    private final RestClient client;
    private final ServiceTokenManager serviceTokenManager;

    public LoggingServiceClient(
        @Qualifier("loggingRestClient") RestClient client,
        ServiceTokenManager serviceTokenManager
    ) {
        this.client = client;
        this.serviceTokenManager = serviceTokenManager;
    }

    @Value("${spring.application.name}")
    private String serviceName;
    @Value("${services.logging.endpoints.log-event}")
    private String logEventUri;

    public void logEvent(Log log) {
        CompletableFuture.runAsync(() -> {
            client.post()
                .uri(logEventUri) // Базовый URL подставится автоматически
                .header("Authorization", "Bearer " + serviceTokenManager.getAccessToken())
                .body(log)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new LoggingServiceRequestException(response.getStatusCode());
                })
                .toBodilessEntity(); // Метод RestClient синхронный, но выполняется в пуле ForkJoinPool
        });
    }

    public void logEvent(LogLevel level, LogReason reason, String message, String payload) {
        logEvent(new Log(Instant.now(), level, serviceName, reason, message, payload));
    }

    public void logEvent(LogLevel level, LogReason reason, String message) {
        logEvent(new Log(Instant.now(), level, serviceName, reason, message, null));
    }
}