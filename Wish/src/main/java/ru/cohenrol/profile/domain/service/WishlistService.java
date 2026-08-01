package ru.cohenrol.profile.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.profile.datasource.model.WishlistEntity;
import ru.cohenrol.profile.datasource.model.WishlistSettingsEntity;
import ru.cohenrol.profile.datasource.repository.WishlistRepository;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.model.WishlistSettingsUpdateRequest;
import ru.cohenrol.profile.domain.model.WishlistUpdateRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final DomainMapper domainMapper;

    @Transactional
    public WishlistEntity create(UUID authorizedUserId, WishlistEntity wishlist) {
        WishlistSettingsEntity settings = new WishlistSettingsEntity();
        settings.setWishlist(wishlist);
        wishlist.setSettings(settings);
        wishlist.setUserId(authorizedUserId);
        return wishlistRepository.save(wishlist);
    }

    @Transactional(readOnly = true)
    public WishlistEntity getWishlistById(UUID wishlistId) {
        return wishlistRepository.findByWishlistId(wishlistId)
            .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));
    }

    @Transactional
    public void deleteWishlist(UUID authorizedUserId, UUID wishlistId) {
        WishlistEntity wishlist = getAndValidateOwner(authorizedUserId, wishlistId);
        wishlistRepository.delete(wishlist);
    }

    @Transactional
    public void updateWishlist(UUID authorizedUserId, UUID wishlistId, WishlistUpdateRequest request) {
        WishlistEntity wishlist = getAndValidateOwner(authorizedUserId, wishlistId);
        domainMapper.updateWishlistFromDto(request, wishlist);
        wishlistRepository.save(wishlist);
    }

    @Transactional(readOnly = true)
    public WishlistSettingsEntity getSettings(UUID authorizedUserId, UUID wishlistId) {
        WishlistEntity wishlist = getAndValidateOwner(authorizedUserId, wishlistId);
        return wishlist.getSettings();
    }

    @Transactional
    public void updateSettings(UUID authorizedUserId, UUID wishlistId, WishlistSettingsUpdateRequest dto) {
        WishlistEntity wishlist = getAndValidateOwner(authorizedUserId, wishlistId);
        WishlistSettingsEntity settings = wishlist.getSettings();
        domainMapper.updateSettingsFromDto(dto, settings);
        wishlistRepository.save(wishlist);
    }

    @Transactional(readOnly = true)
    public WishlistEntity getAndValidateOwner(UUID authorizedUserId, UUID wishlistId) {
        WishlistEntity wishlist = getWishlistById(wishlistId);
        if (!wishlist.getUserId().equals(authorizedUserId)) {
            throw new SecurityException("Access denied: You are not the owner of this wishlist");
        }
        return wishlist;
    }

    @Transactional(readOnly = true)
    public void validateOwner(UUID authorizedUserId, UUID wishlistId) {
        WishlistEntity wishlist = getWishlistById(wishlistId);
        if (!wishlist.getUserId().equals(authorizedUserId)) {
            throw new SecurityException("Access denied: You are not the owner of this wishlist");
        }
    }
}
