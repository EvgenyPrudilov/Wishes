package ru.cohenrol.authserver.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cohenrol.authserver.domain.service.ServiceAuthService;
import ru.cohenrol.authserver.web.model.dto.service.ServiceLoginRequestDto;
import ru.cohenrol.authserver.web.model.dto.service.ServiceLoginResponseDto;

@RestController
@RequestMapping("/api/v1/internals/auth")
@RequiredArgsConstructor
public class InternalsController {
    private final ServiceAuthService serviceAuthService;

    @PostMapping("/wish/login")
    public ResponseEntity<ServiceLoginResponseDto> serviceLogin(
        @Valid @RequestBody ServiceLoginRequestDto requestDto
    ) {
        return ResponseEntity.ok(
            new ServiceLoginResponseDto(
                serviceAuthService.authenticateService(requestDto.getServiceId(), requestDto.getPassword()),
                requestDto.getServiceId()
            )
        );
    }
}


