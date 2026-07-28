package ru.cohenrol.authserver.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cohenrol.authserver.domain.service.*;
import ru.cohenrol.authserver.web.mapper.WebMappers;
import ru.cohenrol.authserver.web.model.dto.*;

@Service
@RequiredArgsConstructor
public class ServicesGate {
    private final JwtSetService jwtSetService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final RefreshTokenService refreshTokenService;
    private final RegistrationService registrationService;
    private final ServiceAuthService serviceAuthService;
    private final WebMappers webMappers;

    public JwtSetResponseDto getJwkSet() {
        return webMappers.toDto(jwtSetService.getJwkSet());
    }

    public void registerNewUser(RegisterRequestDto requestDto) {
        registrationService.registerNewUser(webMappers.toDomain(requestDto));
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        return webMappers.toDto(loginService.login(webMappers.toDomain(requestDto)));
    }

    public RefreshResponseDto useRefreshToken(String refreshToken) {
        return webMappers.toDto(refreshTokenService.useRefreshToken(refreshToken));
    }

    public void logout(String userName) {
        logoutService.logout(userName);
    }
}
