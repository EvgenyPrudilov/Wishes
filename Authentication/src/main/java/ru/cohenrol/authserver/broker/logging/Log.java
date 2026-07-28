package ru.cohenrol.authserver.broker.logging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import ru.cohenrol.authserver.broker.logging.enums.LogReason;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
//    private Instant timestamp;
    private LogLevel level;

    @Value("${spring.application.name:AuthService}")
    private String serviceName;

    private LogReason reason;
    private String message;
    private String payload;


    public Log(LogReason reason, LogLevel level, String message, Object payload) {
        this.level = level;
        this.message = message;
        this.reason = reason;
        this.payload = payload.toString();

//        this.timestamp = Instant.now();
    }
}
