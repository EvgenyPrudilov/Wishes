package ru.cohenrol.profile.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.profile.datasource.enums.BookingPermission;
import ru.cohenrol.profile.datasource.enums.ReservationVisibility;
import ru.cohenrol.profile.datasource.model.ItemEntity;
import ru.cohenrol.profile.datasource.model.WishlistEntity;
import ru.cohenrol.profile.datasource.model.WishlistSettingsEntity;
import ru.cohenrol.profile.datasource.repository.ItemRepository;
import ru.cohenrol.profile.client.ProfileServiceClient;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.model.ItemUpdateRequest;
import ru.cohenrol.profile.web.model.ItemCreateRequestDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final WishlistService wishlistService;
    private final DomainMapper domainMapper;
    private final ProfileServiceClient profileServiceClient;

    @Transactional(readOnly = true)
    public ItemEntity getAndValidateItem(UUID wishlistId, UUID itemId) {
        ItemEntity item = itemRepository.findByItemId(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (!item.getWishlistEntity().getWishlistId().equals(wishlistId)) {
            throw new IllegalArgumentException("Item does not belong to this wishlist");
        }
        return item;
    }

    @Transactional(readOnly = true)
    public ItemEntity getItemFromWishlist(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        ItemEntity item = getAndValidateItem(wishlistId, itemId);
        wishlistService.validateOwner(authorizedUserId, wishlistId);
        return item;
    }

    @Transactional
    public ItemEntity reserveItem(UUID wishlistId, UUID itemId) {
        ItemEntity item = getAndValidateItem(wishlistId, itemId);
        if (item.isReserved()) {
            throw new IllegalStateException("Item already reserved");
        }
        item.setReserved(true);
        return itemRepository.save(item);
    }

    @Transactional
    public void addItem(UUID authorizedUserId, UUID wishlistId, ItemCreateRequestDto dto) {
        WishlistEntity wishlist = wishlistService.getAndValidateOwner(authorizedUserId, wishlistId);
        ItemEntity item = domainMapper.toItemEntity(dto);
        item.setWishlistEntity(wishlist);
        itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        wishlistService.validateOwner(authorizedUserId, wishlistId);
        ItemEntity item = getAndValidateItem(wishlistId, itemId);
        itemRepository.delete(item);
    }

    @Transactional
    public void updateItem(UUID authorizedUserId, UUID wishlistId, UUID itemId, ItemUpdateRequest body) {
        wishlistService.validateOwner(authorizedUserId, wishlistId);
        ItemEntity item = getAndValidateItem(wishlistId, itemId);
        domainMapper.updateItemFromDto(body, item);
        itemRepository.save(item);
    }

    @Transactional
    public void reserveItemAsGuest(UUID wishlistId, UUID itemId, String reservedBy) {
        reserveItem(null, wishlistId, itemId, reservedBy);

    }

    @Transactional
    public void reserveItemAsAuthorized(UUID authorizedUserId, UUID wishlistId, UUID itemId, String reservedBy) {
        reserveItem(authorizedUserId, wishlistId, itemId, reservedBy);
    }

    @Transactional
    public void reserveItemAsAuthorized(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        reserveItem(authorizedUserId, wishlistId, itemId, null);
    }

    @Transactional
    public void reserveItem(UUID authorizedUserId, UUID wishlistId, UUID itemId, String reservedBy) {
        ItemEntity item = getAndValidateItem(wishlistId, itemId);
        if (item.isReserved()) {
            throw new IllegalStateException("Item already reserved");
        }

        WishlistEntity wishlist = item.getWishlistEntity();
        WishlistSettingsEntity settings = wishlist.getSettings();
        if (settings.getBookingPrivacy() == BookingPermission.FRIENDS) {
            if (authorizedUserId == null || profileServiceClient.checkFriendship(authorizedUserId, wishlist.getUserId())) {
                throw new RuntimeException("Not friend");
            }
        }
        if (settings.getVisibilityMode() == ReservationVisibility.WITH_NAMES && reservedBy == null) {
            throw new RuntimeException("No name");
        }

        item.setReservedBy_Name(reservedBy);
        item.setReservedBy(authorizedUserId);
        item.setReserved(true);
    }

    @Transactional
    public void unreserveItemAsAuthorized(UUID authorizedUserId, UUID wishlistId, UUID itemId) {
        ItemEntity item = getAndValidateItem(wishlistId, itemId);
        if (!item.isReserved()) {
            throw new IllegalStateException("Item not reserved");
        }

        WishlistEntity wishlist = item.getWishlistEntity();
        WishlistSettingsEntity settings = wishlist.getSettings();
        if (settings.getBookingPrivacy() == BookingPermission.FRIENDS) {
            if (authorizedUserId == null || authorizedUserId != item.getReservedBy()) {
                throw new RuntimeException("Not friend");
            }
        }

        item.setReservedBy_Name(null);
        item.setReservedBy(null);
        item.setReserved(false);
    }

    @Transactional
    public void unreserveItemAsGuest(UUID wishlistId, UUID itemId) {
        this.unreserveItemAsAuthorized(null, wishlistId, itemId);
    }
}