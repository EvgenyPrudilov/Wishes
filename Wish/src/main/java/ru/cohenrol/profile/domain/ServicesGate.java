package ru.cohenrol.profile.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.service.ItemService;
import ru.cohenrol.profile.domain.service.WishlistService;
import ru.cohenrol.profile.web.mapper.WebMapper;
import ru.cohenrol.profile.web.model.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicesGate {
    private final WebMapper webMapper;
    private final DomainMapper domainMapper;
    private final WishlistService wishlistService;
    private final ItemService itemService;

    public GetWishlistResponseDto getWishlistById(UUID wishlistId) {
        return domainMapper.toGetWishlistResponseDto(wishlistService.getWishlistById(wishlistId));
    }

    public WishlistCreateResponseDto createWishlist(UUID currentUserId, WishlistCreateRequestDto wishlistCreateRequestDto) {
        return domainMapper.toWishlistCreateResponseDto(
            wishlistService.create(currentUserId, domainMapper.toWishlistEntity(wishlistCreateRequestDto))
        );
    }

    public void deleteWishlist(UUID authorizedUserId, UUID wishlistId) {
        wishlistService.deleteWishlist(authorizedUserId, wishlistId);
    }

    public void updateWishlist(UUID authorizedUserId, UUID wishlistId, WishlistUpdateRequestDto body) {
        wishlistService.updateWishlist(authorizedUserId, wishlistId, domainMapper.toWishlistUpdateRequest(body));
    }

    public WishlistSettingsResponseDto getSettings(UUID authorizedUserId, UUID wishlistId) {
        return domainMapper.toWishlistSettingsResponseDto(
            wishlistService.getSettings(authorizedUserId, wishlistId)
        );
    }

    public void updateSettings(UUID authorizedUserId, UUID wishlistId, WishlistSettingsUpdateRequestDto body) {
        wishlistService.updateSettings(authorizedUserId, wishlistId, domainMapper.toWishlistSettingsUpdateRequest(body));
    }



    public void reserveItemAsGuest(UUID wishlistId, UUID itemId, ReserveItemRequestDto body) {
        itemService.reserveItemAsGuest(wishlistId, itemId, body.getReservedBy());
    }

    public void reserveItemAsAuthorized(UUID authorizedUserId, UUID wishlistId, UUID itemId, ReserveItemRequestDto body) {
        itemService.reserveItemAsAuthorized(authorizedUserId, wishlistId, itemId, body.getReservedBy());
    }

    public void reserveItemAsAuthorized(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        itemService.reserveItemAsAuthorized(authorizedUserId, wishlistId, itemId);
    }

    public void addItemToWishlist(UUID authorizedUserId, UUID wishlistId, ItemCreateRequestDto dto) {
        itemService.addItem(authorizedUserId, wishlistId, dto);
    }

    public GetItemResponseDto getItemFromWishlist(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        return domainMapper.toGetItemResponseDto(
            itemService.getItemFromWishlist(authorizedUserId, wishlistId, itemId)
        );
    }

    public void deleteItemFromWishlist(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        itemService.deleteItem(authorizedUserId, wishlistId, itemId);
    }

    public void updateItemInWishlist(UUID authorizedUserId, UUID wishlistId, UUID itemId, ItemUpdateRequestDto body) {
        itemService.updateItem(authorizedUserId, wishlistId, itemId, domainMapper.toItemUpdateRequest(body));
    }

    public void unreserveItemAsAuthorized(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        itemService.unreserveItemAsAuthorized(authorizedUserId, wishlistId, itemId);
    }

    public void unreserveItemAsGuest(UUID wishlistId, UUID itemId) {
        itemService.unreserveItemAsGuest(wishlistId, itemId);
    }
}
