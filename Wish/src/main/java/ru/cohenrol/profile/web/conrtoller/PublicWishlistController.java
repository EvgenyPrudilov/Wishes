package ru.cohenrol.profile.web.conrtoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.profile.domain.ServicesGate;
import ru.cohenrol.profile.web.model.ReserveItemRequestDto;
import ru.cohenrol.profile.web.model.GetWishlistResponseDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/wishlist")
@RequiredArgsConstructor
public class PublicWishlistController {
    private final ServicesGate servicesGate;

    @GetMapping("/{wishlistId}")
    public ResponseEntity<GetWishlistResponseDto> getWishlist(
        @PathVariable UUID wishlistId
    ) {
        GetWishlistResponseDto wishlist = servicesGate.getWishlistById(wishlistId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/{wishlistId}/item/{itemId}/reserved")
    public ResponseEntity<Void> reserveItemAsGuest(
        @PathVariable UUID wishlistId,
        @PathVariable UUID itemId,
        @RequestBody(required = false) ReserveItemRequestDto reserveItemRequestDto,
        @AuthenticationPrincipal UUID authorizedUserId
    ) {
        if (authorizedUserId != null) {
            servicesGate.reserveItemAsAuthorized(authorizedUserId, wishlistId, itemId, reserveItemRequestDto);
        } else {
            servicesGate.reserveItemAsGuest(wishlistId, itemId, reserveItemRequestDto);
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{wishlistId}/item/{itemId}/reserved")
    public ResponseEntity<Void> unreserveItemAsGuest(
        @PathVariable UUID wishlistId,
        @PathVariable UUID itemId,
        @AuthenticationPrincipal UUID authorizedUserId
    ) {
        if (authorizedUserId != null) {
            servicesGate.unreserveItemAsAuthorized(authorizedUserId, wishlistId, itemId);
        } else {
            servicesGate.unreserveItemAsGuest(wishlistId, itemId);
        }

        return ResponseEntity.noContent().build();
    }
}