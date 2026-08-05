package ru.cohenrol.authserver.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private UUID uuid;
    private String username;
    private String email;
    private String password;
    private boolean enabled;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}