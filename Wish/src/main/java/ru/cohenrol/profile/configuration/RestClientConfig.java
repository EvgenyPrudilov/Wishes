package ru.cohenrol.profile.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${services.profile.base-url}")
    private String profileBaseUrl;
    @Value("${services.profile.connect-timeout-ms}")
    private int connectTimeout;
    @Value("${services.profile.read-timeout-ms}")
    private int readTimeout;

    @Bean
    @RefreshScope
    public RestClient profileRestClient() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return RestClient.builder()
            .baseUrl(profileBaseUrl) // Базовый URL теперь задан глобально
            .requestFactory(requestFactory)
            .build();
    }

    @Value("${services.logging.url}")
    private String loggingServiceBase;
    @Value("${services.logging.connect-timeout-ms:2000}") // Дефолт 2с, если нет в Config Cloud
    private int loggingConnectTimeout;
    @Value("${services.logging.read-timeout-ms:3000}")    // Дефолт 3с
    private int loggingReadTimeout;

    @Bean
    @RefreshScope
    public RestClient loggingRestClient() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(loggingConnectTimeout);
        requestFactory.setReadTimeout(loggingReadTimeout);

        return RestClient.builder()
            .baseUrl(loggingServiceBase)
            .requestFactory(requestFactory)
            .build();
    }
}