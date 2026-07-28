package ru.cohenrol.profile.client.model;

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
