package ru.cohenrol.profile.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.cohenrol.profile.datasource.enums.BookingPermission;
import ru.cohenrol.profile.datasource.enums.PrivacyType;
import ru.cohenrol.profile.datasource.enums.ReservationVisibility;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistSettingsResponseDto {
    private ReservationVisibility visibilityMode;
    private PrivacyType viewPrivacy;
    private BookingPermission bookingPrivacy;
    private boolean showProfileLink;
}
