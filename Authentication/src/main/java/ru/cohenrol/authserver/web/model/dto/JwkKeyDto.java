package ru.cohenrol.authserver.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwkKeyDto {
    private String kty;
    private String crv;
    private String x;
    private String y;
    private String kid;
}