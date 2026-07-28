package ru.cohenrol.authserver.broker.logging.events;

import org.springframework.boot.logging.LogLevel;
import ru.cohenrol.authserver.broker.logging.enums.LogReason;

public class LogoutLogEvent extends LogEvent {
    public LogoutLogEvent(LogLevel level, String message, Object payload) {
        super(LogReason.LOGOUT, level, message, payload);
    }
}