package ru.cohenrol.loggingservice.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cohenrol.loggingservice.domain.ServicesGate;
import ru.cohenrol.loggingservice.web.model.AddLogRequestDto;
import ru.cohenrol.loggingservice.web.model.AddLogsRequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internal/logging")
public class InternalLogsController {
    private final ServicesGate servicesGate;

    @PostMapping("/log")
    public ResponseEntity<Void> createLog(@Valid @RequestBody AddLogRequestDto request) {
        servicesGate.addLog(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logs")
    public ResponseEntity<Void> createLogs(@Valid @RequestBody AddLogsRequestDto request) {
        servicesGate.addLogs(request);
        return ResponseEntity.ok().build();
    }
}

