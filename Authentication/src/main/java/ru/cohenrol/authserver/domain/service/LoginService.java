package ru.cohenrol.authserver.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.authserver.broker.logging.events.LoginLogEvent;
import ru.cohenrol.authserver.datasource.repository.CustomUserRepository;
import ru.cohenrol.authserver.domain.model.LoginRequest;
import ru.cohenrol.authserver.domain.model.LoginResponse;
import ru.cohenrol.authserver.domain.model.User;
import ru.cohenrol.authserver.domain.token.JwtProvider;
import ru.cohenrol.authserver.web.mapper.WebMappers;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
//    private final KafkaLogProducerService kafkaLogProducerService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        String userName = request.getUsername();

        User user = customUserRepository.findByUsername(userName)
            .orElseThrow(() -> new BadCredentialsException("Неверное имя пользователя или пароль"));

        if (!user.isEnabled()) {
            throw new BadCredentialsException("Пожалуйста, подтвердите ваш email");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Неверное имя пользователя или пароль");
        }

        LoginResponse loginResponse = new LoginResponse(
            jwtProvider.generateAccessToken(userName),
            refreshTokenService.generateRefreshToken(user),
            userName
        );

//        kafkaLogProducerService.send(new LoginLogEvent(
//                LogLevel.INFO,
//                "User " + userName + " has logged in.",
//                user
//        ));
        return loginResponse;
    }
}