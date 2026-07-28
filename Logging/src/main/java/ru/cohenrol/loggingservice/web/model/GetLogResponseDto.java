package ru.cohenrol.loggingservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cohenrol.loggingservice.logging.enums.LogLevel;
import ru.cohenrol.loggingservice.logging.enums.LogReason;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLogResponseDto {
    private Instant timestamp;
    private LogLevel level;
    private String userName;
    private String serviceName;
    private LogReason reason;
    private String message;
    private String payload;
}
