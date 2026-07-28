package ru.cohenrol.authserver.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.authserver.domain.ServicesGate;
import org.springframework.beans.factory.annotation.Value;
import ru.cohenrol.authserver.web.model.dto.*;

@RestController
@RequiredArgsConstructor
@RefreshScope
@RequestMapping("/api/v1/public/auth")
public class PublicController {
    private final ServicesGate servicesGate;

    @Value("${app.refresh-token.path}")
    private String refreshPath;
    @Value("${app.refresh-token.max-age-s}")
    private Long refreshMaxAge;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
        @Valid @RequestBody RegisterRequestDto request
    ) {
        servicesGate.registerNewUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
        @Valid @RequestBody LoginRequestDto requestDto
    ) {
        LoginResponseDto loginResponseDto = servicesGate.login(requestDto);
        ResponseCookie refreshCookie = createRefreshCookie(loginResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new TokenResponseDto(
                    loginResponseDto.getAccessToken(),
                    loginResponseDto.getUsername())
                );
    }

    @GetMapping("/jwks")
    public ResponseEntity<JwtSetResponseDto> getJwkSet() {
        return ResponseEntity.ok(servicesGate.getJwkSet());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(
        @CookieValue(name = "refreshToken") String refreshToken
//        , @Valid @RequestBody RefreshRequestDto refreshRequestDto
    ) {
        RefreshResponseDto refreshResponseDto = servicesGate.useRefreshToken(refreshToken);
        ResponseCookie refreshCookie = createRefreshCookie(refreshResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new TokenResponseDto(
                    refreshResponseDto.getAccessToken(),
                    refreshResponseDto.getUsername())
                );
    }

    private ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path(refreshPath)
                .maxAge(refreshMaxAge)
                .sameSite("Strict")
                .build();
    }
}
