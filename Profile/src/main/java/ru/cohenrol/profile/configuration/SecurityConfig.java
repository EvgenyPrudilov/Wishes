package ru.cohenrol.profile.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${SPRING_AUTHENTICATION_SERVICE_JWK_URI}")
    private String jwkSetUri;
    @Value("${app.user-access-token-expected-to-have.issuer}")
    private String expectedIssuer;
    @Value("${app.user-access-token-expected-to-have.audience}")
    private String expectedAudience;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/v1/public/**").permitAll()
                    .anyRequest().authenticated()
                )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder()) // Подключаем кастомный декодер с валидаторами
                    .jwtAuthenticationConverter(jwtInstance -> {
                        UUID userId = UUID.fromString(jwtInstance.getSubject());
                        List<String> roles = jwtInstance.getClaimAsStringList("roles");
                        List<SimpleGrantedAuthority> authorities = java.util.Collections.emptyList();
                        if (roles != null) {
                            authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toList());
                        }

                        return new UsernamePasswordAuthenticationToken(userId, jwtInstance, authorities);
                    })
                )
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();

        OAuth2TokenValidator<Jwt> audienceValidator = token -> {
            // Безопасно проверяем, входит ли ваш конкретный сервис в список разрешенных
            if (token.getAudience() != null && token.getAudience().contains(expectedAudience)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_audience", "Этот сервис отсутствует в аудитории токена", null)
            );
        };

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(expectedIssuer);
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator));

        return jwtDecoder;
    }
}
