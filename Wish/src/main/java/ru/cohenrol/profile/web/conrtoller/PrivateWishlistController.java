package ru.cohenrol.profile.web.conrtoller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.profile.domain.ServicesGate;
import ru.cohenrol.profile.web.model.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private/wishlist")
@RequiredArgsConstructor
public class PrivateWishlistController {
    private final ServicesGate servicesGate;

    @PostMapping
    public ResponseEntity<WishlistCreateResponseDto> createWishlist(
        @AuthenticationPrincipal UUID authorizedUserId,
        @RequestBody WishlistCreateRequestDto wishlistCreateRequestDto
    ) {
        WishlistCreateResponseDto wishlist = servicesGate.createWishlist(authorizedUserId, wishlistCreateRequestDto);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> deleteWishlist(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId
    ) {
        servicesGate.deleteWishlist(authorizedUserId, wishlistId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{wishlistId}")
    public ResponseEntity<Void> updateWishlist(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @RequestBody WishlistUpdateRequestDto wishlistUpdateRequestDto
    ) {
        servicesGate.updateWishlist(authorizedUserId, wishlistId, wishlistUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{wishlistId}/settings")
    public ResponseEntity<WishlistSettingsResponseDto> getSettings(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId
    ) {
        WishlistSettingsResponseDto settings = servicesGate.getSettings(authorizedUserId, wishlistId);
        return ResponseEntity.ok(settings);
    }

    @PutMapping("/{wishlistId}/settings")
    public ResponseEntity<WishlistSettingsResponseDto> updateSettings(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @RequestBody WishlistSettingsUpdateRequestDto wishlistSettingsUpdateRequestDto
    ) {
        servicesGate.updateSettings(authorizedUserId, wishlistId, wishlistSettingsUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{wishlistId}/item")
    public ResponseEntity<Void> addItem(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @RequestBody ItemCreateRequestDto itemCreateRequestDto
    ) {
        servicesGate.addItemToWishlist(authorizedUserId, wishlistId, itemCreateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{wishlistId}/item/{itemId}")
    public ResponseEntity<GetItemResponseDto> getItem(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @PathVariable UUID itemId
    ) {
        GetItemResponseDto item = servicesGate.getItemFromWishlist(authorizedUserId, wishlistId, itemId);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{wishlistId}/item/{itemId}")
    public ResponseEntity<Void> deleteItem(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @PathVariable UUID itemId
    ) {
        servicesGate.deleteItemFromWishlist(authorizedUserId, wishlistId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{wishlistId}/item/{itemId}")
    public ResponseEntity<Void> updateItem(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @PathVariable UUID itemId,
        @RequestBody ItemUpdateRequestDto itemUpdateRequestDto
    ) {
        servicesGate.updateItemInWishlist(authorizedUserId, wishlistId, itemId, itemUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{wishlistId}/item/{itemId}/reserved")
    public ResponseEntity<GetItemResponseDto> reserveItemAsAuthorized(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @PathVariable UUID itemId
    ) {
        servicesGate.reserveItemAsAuthorized(authorizedUserId, wishlistId, itemId);
        return ResponseEntity.noContent().build();
    }
}