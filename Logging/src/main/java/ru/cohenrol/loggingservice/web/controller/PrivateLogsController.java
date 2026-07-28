package ru.cohenrol.loggingservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.loggingservice.domain.ServicesGate;
import ru.cohenrol.loggingservice.logging.enums.LogLevel;
import ru.cohenrol.loggingservice.logging.enums.LogReason;
import ru.cohenrol.loggingservice.web.model.GetLogsResponseDto;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/private/logging/logs")
public class PrivateLogsController {
    private final ServicesGate servicesGate;

    @GetMapping
    public ResponseEntity<GetLogsResponseDto> getLogs(
        @RequestParam(required = false) String service,
        @RequestParam(required = false) LogLevel level,
        @RequestParam(required = false) LogReason reason,
        @RequestParam(required = false, name = "user_name") String userName,
        @RequestParam(required = false) Instant from,
        @RequestParam(required = false) Instant to
    ) {
        return ResponseEntity.ok(servicesGate.getLogs(service, level, reason, userName, from, to));
    }
}

