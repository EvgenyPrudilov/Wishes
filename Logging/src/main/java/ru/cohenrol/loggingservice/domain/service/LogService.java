package ru.cohenrol.loggingservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cohenrol.loggingservice.datasource.model.LogEntity;
import ru.cohenrol.loggingservice.datasource.repository.LogRepository;
import ru.cohenrol.loggingservice.logging.enums.LogLevel;
import ru.cohenrol.loggingservice.logging.enums.LogReason;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    public void addLog(LogEntity log) {
        if (log.getTimestamp() == null) {
            log.setTimestamp(Instant.now());
        }

        logRepository.save(log);
    }

    public List<LogEntity> getLogs(
        String service,
        LogLevel level,
        LogReason reason,
        String user,
        Instant from,
        Instant to
    ) {
        List<LogEntity> logEntityList = logRepository.findLogsByFilters(service, level, reason, user, from, to);
        return logEntityList;
    }

    public void addLogs(List<LogEntity> logs) {
        logs.forEach(this::addLog);
    }
}