package ru.cohenrol.profile.domain.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WishlistUpdateRequest {
    private String title;
    private String description;
    private LocalDate eventDate;
}