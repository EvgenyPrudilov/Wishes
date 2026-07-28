package ru.cohenrol.profile.web.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WishlistUpdateRequestDto {
    private String title;
    private String description;
    private LocalDate eventDate;
}