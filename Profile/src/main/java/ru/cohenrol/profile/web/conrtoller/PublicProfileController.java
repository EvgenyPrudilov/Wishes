package ru.cohenrol.profile.web.conrtoller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.profile.domain.ServicesGate;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;
import ru.cohenrol.profile.web.model.dto.UserProfileWithFriendsResponseDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicProfileController {
    private final ServicesGate servicesGate;

    @GetMapping("/profile/{targetUserId}")
    public ResponseEntity<UserProfileResponseDto> getProfile(
        @AuthenticationPrincipal UUID currentUserId,
        @NonNull @PathVariable UUID targetUserId
    ) {
        UserProfileResponseDto profile = servicesGate.getProfile(currentUserId, targetUserId);
        return ResponseEntity.ok(profile);
    }
}