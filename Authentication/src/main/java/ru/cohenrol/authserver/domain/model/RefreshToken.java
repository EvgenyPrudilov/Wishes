package ru.cohenrol.authserver.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private String token;
    private Instant expiryDate;
    private User user;

    public boolean isExpired() {
        return this.expiryDate.isBefore(Instant.now());
    }
}