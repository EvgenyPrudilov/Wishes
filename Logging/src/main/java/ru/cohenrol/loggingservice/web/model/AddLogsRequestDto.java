package ru.cohenrol.loggingservice.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cohenrol.loggingservice.logging.enums.LogLevel;
import ru.cohenrol.loggingservice.logging.enums.LogReason;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLogsRequestDto {
    private List<AddLogRequestDto> logs;
}
