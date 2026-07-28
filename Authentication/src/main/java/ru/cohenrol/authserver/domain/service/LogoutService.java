package ru.cohenrol.authserver.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.authserver.broker.logging.events.LogoutLogEvent;
import ru.cohenrol.authserver.datasource.repository.CustomRefreshTokenRepository;
import ru.cohenrol.authserver.datasource.repository.CustomUserRepository;
import ru.cohenrol.authserver.domain.token.JwtProvider;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final CustomRefreshTokenRepository refreshTokenRepository;
    private final CustomUserRepository customUserRepository;
//    private final KafkaLogProducerService kafkaLogProducerService;

    @Transactional
    public void logout(String userName) {
        customUserRepository.findByUsername(userName).ifPresent(user -> {
            refreshTokenRepository.deleteByUser_Id(user.getId());
//            kafkaLogProducerService.send(
//                new LogoutLogEvent(LogLevel.INFO, "User " + userName + " has logged out.", user)
//            );
        });
    }
}
