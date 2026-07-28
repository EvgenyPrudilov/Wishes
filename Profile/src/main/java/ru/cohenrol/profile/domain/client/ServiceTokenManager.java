package ru.cohenrol.profile.domain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.cohenrol.profile.web.model.client.ServiceLoginRequestDto;
import ru.cohenrol.profile.web.model.client.ServiceLoginResponseDto;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@RefreshScope
public class ServiceTokenManager {
    @Value("${services.auth.url}")
    private String authServerBase;
    @Value("${services.auth.endpoints.login}")
    private String loginUri;

    @Value("${app.auth-server-credentials.service-name}")
    private String serviceName;
    @Value("${app.auth-server-credentials.password}")
    private String password;

    private String cachedToken;
    private Instant tokenExpiry;
    private final RestClient client = RestClient.create();

    public synchronized String getAccessToken() {
        if (cachedToken != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry.minusSeconds(60))) {
            return cachedToken;
        }

        refreshJwtToken();
        return cachedToken;
    }

    private void refreshJwtToken() {
        try {
            ServiceLoginRequestDto requestBody = new ServiceLoginRequestDto(serviceName, password);

            ServiceLoginResponseDto responseBody = client.post()
                .uri(authServerBase + loginUri)
                .body(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new RuntimeException("AuthServer вернул ошибку: " + response.getStatusCode());
                })
                .body(ServiceLoginResponseDto.class);

            if (responseBody != null && responseBody.getAccessToken() != null && responseBody.getServiceId() != null) {
                cachedToken = responseBody.getAccessToken();
                tokenExpiry = Instant.now().plusSeconds(15 * 60);
                System.out.println(">>> Токен: " + cachedToken);
            } else {
                throw new RuntimeException("В ответе AuthServer отсутствует accessToken");
            }
        } catch (Exception e) {
            throw new RuntimeException("Критическая ошибка при запросе токена через RestClient", e);
        }
    }
}