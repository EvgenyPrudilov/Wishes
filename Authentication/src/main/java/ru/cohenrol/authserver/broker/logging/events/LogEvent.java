package ru.cohenrol.authserver.broker.logging.events;

import org.springframework.boot.logging.LogLevel;
import ru.cohenrol.authserver.broker.logging.Log;
import ru.cohenrol.authserver.broker.logging.enums.LogReason;

public class LogEvent extends Log {
    public LogEvent(LogReason reason, LogLevel level, String message, Object payload) {
        super(reason, level, message, payload);
    }
}
