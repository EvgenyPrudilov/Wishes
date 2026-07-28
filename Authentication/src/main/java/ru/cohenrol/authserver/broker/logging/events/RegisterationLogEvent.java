package ru.cohenrol.authserver.broker.logging.events;

import org.springframework.boot.logging.LogLevel;
import ru.cohenrol.authserver.broker.logging.enums.LogReason;

public class RegisterationLogEvent extends LogEvent {
    public RegisterationLogEvent(LogLevel level, String message, Object payload) {
        super(LogReason.REGISTRATION, level, message, payload);
    }
}
