package ru.cohenrol.profile.web.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLoginResponseDto {
    private String accessToken;
    private String serviceId;
}