package ru.cohenrol.loggingservice.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.cohenrol.loggingservice.logging.enums.LogLevel;
import ru.cohenrol.loggingservice.logging.enums.LogReason;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLogRequestDto {
    private Instant timestamp;

    @NotNull(message = "Уровень лога (level) обязателен")
    private LogLevel level;
//    private String level;

    @NotNull(message = "Имя сервиса обязателено")
    private String serviceName;

    @NotNull(message = "Причина лога (reason) обязательна")
    private LogReason reason;
//    private String reason;

    @NotNull(message = "Имя пользователя обязателено")
    private String userName;

    @NotNull(message = "Сообщение лога не должно быть пустым")
    private String message;

    private String payload;
}
