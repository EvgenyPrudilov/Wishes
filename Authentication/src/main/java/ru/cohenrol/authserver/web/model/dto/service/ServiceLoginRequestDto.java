package ru.cohenrol.authserver.web.model.dto.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServiceLoginRequestDto {
    @NotBlank(message = "Service ID не может быть пустым")
    private String serviceId;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}