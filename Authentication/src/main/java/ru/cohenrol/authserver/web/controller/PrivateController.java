package ru.cohenrol.authserver.web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cohenrol.authserver.domain.ServicesGate;
import ru.cohenrol.authserver.domain.service.LogoutService;

@RestController
@RequiredArgsConstructor
@RefreshScope
@RequestMapping("/api/v1/private/auth")
public class PrivateController {
    private final ServicesGate servicesGate;

    @Value("${app.refresh-token.path}")
    private String refreshPath;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @AuthenticationPrincipal String userName
    ) {
        servicesGate.logout(userName);
        ResponseCookie clearRefreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path(refreshPath)
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearRefreshCookie.toString())
                .build();
    }
}
