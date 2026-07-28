package ru.cohenrol.authserver.broker.logging.events;

import org.springframework.boot.logging.LogLevel;
import ru.cohenrol.authserver.broker.logging.enums.LogReason;

public class LoginLogEvent extends LogEvent {
    public LoginLogEvent(LogLevel level, String message, Object payload) {
        super(LogReason.LOGIN, level, message, payload);
    }
}