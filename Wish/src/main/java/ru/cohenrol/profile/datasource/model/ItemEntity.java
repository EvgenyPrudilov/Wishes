package ru.cohenrol.profile.datasource.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "items")
@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", unique = true, nullable = false, updatable = false)
    private UUID itemId = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private WishlistEntity wishlist;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String link;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "is_reserved")
    private boolean reserved = false;

    @Column(name = "reserved_by", nullable = false)
    private UUID reservedBy = UUID.randomUUID();

    @Column(name = "reserved_by__name", length = 100)
    private String reservedBy_Name;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}