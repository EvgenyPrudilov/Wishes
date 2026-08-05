package ru.cohenrol.authserver.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.authserver.broker.logging.events.RefreshLogEvent;
import ru.cohenrol.authserver.datasource.repository.CustomRefreshTokenRepository;
import ru.cohenrol.authserver.datasource.repository.CustomUserRepository;
import ru.cohenrol.authserver.domain.exception.TokenRefreshException;
import ru.cohenrol.authserver.domain.model.RefreshResponse;
import ru.cohenrol.authserver.domain.model.RefreshToken;
import ru.cohenrol.authserver.domain.model.User;
import ru.cohenrol.authserver.domain.token.JwtProvider;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@RefreshScope
public class RefreshTokenService {
    @Value("${app.jwt.users.refresh-expiration-ms}")
    private Long refreshExpirationMs;
    private final CustomRefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
//    private final KafkaLogProducerService kafkaLogProducerService;

    @Transactional
    public String generateRefreshToken(User user) {
//        User user = customUserRepository.findByUsername(username)
//            .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        refreshTokenRepository.deleteByUser_Id(user.getId());
        refreshTokenRepository.flush();

        String tokenItself = jwtProvider.generateRefreshToken();
        refreshTokenRepository.save(new RefreshToken(
            tokenItself,
            Instant.now().plusMillis(refreshExpirationMs),
            user
        ));

        return tokenItself;
    }

    @Transactional
    public RefreshResponse useRefreshToken(String token) {
        RefreshToken oldRefreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Refresh-токен не найден в системе. Войдите заново."));

        if (oldRefreshToken.isExpired()) {
            refreshTokenRepository.deleteByUser_Id(oldRefreshToken.getUser().getId());
            throw new TokenRefreshException("Срок действия сессии истек. Пожалуйста, авторизуйтесь заново.");
        }

        User user = oldRefreshToken.getUser();
        String userName = user.getUsername();
        RefreshResponse refreshResponse = new RefreshResponse(
                jwtProvider.generateAccessToken(user.getUuid()),
                generateRefreshToken(user),
                userName
        );

//        kafkaLogProducerService.send(
//                new RefreshLogEvent(LogLevel.INFO, "User " + userName + " has refreshed the tokens.", user)
//        );
        return refreshResponse;
    }



}