package ru.cohenrol.profile.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWishlistResponseDto {
    private UUID wishlistId;
    private UUID userId;
    private String title;
    private String description;
    private LocalDate eventDate;
    private Instant createdAt;
    private WishlistSettingsResponseDto settings;
    private List<GetItemResponseDto> items;
}