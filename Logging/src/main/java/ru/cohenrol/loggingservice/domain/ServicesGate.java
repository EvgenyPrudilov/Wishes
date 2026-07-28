package ru.cohenrol.loggingservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cohenrol.loggingservice.domain.service.LogService;
import ru.cohenrol.loggingservice.logging.enums.LogLevel;
import ru.cohenrol.loggingservice.logging.enums.LogReason;
import ru.cohenrol.loggingservice.web.mapper.WebMapper;
import ru.cohenrol.loggingservice.web.model.AddLogRequestDto;
import ru.cohenrol.loggingservice.web.model.AddLogsRequestDto;
import ru.cohenrol.loggingservice.web.model.GetLogsResponseDto;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ServicesGate {
    private final LogService logService;
    private final WebMapper webMapper;

    public void addLog(AddLogRequestDto addLogRequestDto) {
        logService.addLog(webMapper.toLog(addLogRequestDto));
    }

    public void addLogs(AddLogsRequestDto addLogsRequestDto) {
        logService.addLogs(webMapper.toLog(addLogsRequestDto));
    }

    public GetLogsResponseDto getLogs(String service, LogLevel level, LogReason reason, String userName, Instant from, Instant to) {
        return new GetLogsResponseDto(
            logService.getLogs(service, level, reason, userName, from, to)
                .stream()
                .map(webMapper::toDto)
                .toList()
        );
    }
}
