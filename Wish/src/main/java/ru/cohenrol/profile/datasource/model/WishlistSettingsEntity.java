package ru.cohenrol.profile.datasource.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.cohenrol.profile.datasource.enums.BookingPermission;
import ru.cohenrol.profile.datasource.enums.PrivacyType;
import ru.cohenrol.profile.datasource.enums.ReservationVisibility;

@Entity
@Table(name = "wishlist_settings")
@Getter @Setter
@NoArgsConstructor
public class WishlistSettingsEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Указывает JPA использовать первичный ключ Wishlist как PK и FK одновременно
    @JoinColumn(name = "wishlist_id")
    private WishlistEntity wishlistEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility_mode", nullable = false)
    private ReservationVisibility visibilityMode = ReservationVisibility.WITHOUT_NAMES;

    @Enumerated(EnumType.STRING)
    @Column(name = "view_privacy", nullable = false)
    private PrivacyType viewPrivacy = PrivacyType.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_privacy", nullable = false)
    private BookingPermission bookingPrivacy = BookingPermission.EVERYONE;

    @Column(name = "show_profile_link", nullable = false)
    private boolean showProfileLink = true;
}
