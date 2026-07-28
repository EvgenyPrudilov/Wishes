package ru.cohenrol.profile.domain.model;

import lombok.Data;
import ru.cohenrol.profile.datasource.enums.BookingPermission;
import ru.cohenrol.profile.datasource.enums.PrivacyType;
import ru.cohenrol.profile.datasource.enums.ReservationVisibility;

@Data
public class WishlistSettingsUpdateRequest {
    private ReservationVisibility visibilityMode;
    private PrivacyType viewPrivacy;
    private BookingPermission bookingPrivacy;
    private boolean showProfileLink;
}