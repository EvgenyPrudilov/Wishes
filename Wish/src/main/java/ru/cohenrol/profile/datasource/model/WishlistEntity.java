package ru.cohenrol.profile.datasource.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "wishlists")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class WishlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wishlist_id", unique = true, nullable = false)
    private UUID wishlistId = UUID.randomUUID();

    @Column(name = "user_id", unique = true, nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @OneToOne(mappedBy = "wishlist", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private WishlistSettingsEntity settings;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> itemEntities = new ArrayList<>();
}
