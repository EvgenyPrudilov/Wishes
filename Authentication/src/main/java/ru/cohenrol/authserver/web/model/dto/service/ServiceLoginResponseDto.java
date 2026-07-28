package ru.cohenrol.authserver.web.model.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServiceLoginResponseDto {
    private String accessToken;
    private String serviceId;
}