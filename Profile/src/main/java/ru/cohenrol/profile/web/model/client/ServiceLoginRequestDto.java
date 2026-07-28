package ru.cohenrol.profile.web.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLoginRequestDto {
    private String serviceId;
    private String password;
}
