package ru.cohenrol.authserver.broker.logging.events;

import org.springframework.boot.logging.LogLevel;
import ru.cohenrol.authserver.broker.logging.enums.LogReason;

public class RefreshLogEvent extends LogEvent {
    public RefreshLogEvent(LogLevel level, String message, Object payload) {
        super(LogReason.REFRESH, level, message, payload);
    }
}