package ru.cohenrol.profile.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistCreateResponseDto {
    private UUID wishlistId;
    private Instant createdAt;
    private WishlistSettingsResponseDto settings;
}