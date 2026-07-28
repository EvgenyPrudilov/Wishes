package ru.cohenrol.profile.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cohenrol.profile.domain.model.enums.LogLevel;
import ru.cohenrol.profile.domain.model.enums.LogReason;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private LogLevel level;
    private String serviceName;
    private LogReason reason;
    private String message;
    private String payload;
}
