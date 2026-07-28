package ru.cohenrol.authserver.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse { // Убрали слово 'abstract'
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}