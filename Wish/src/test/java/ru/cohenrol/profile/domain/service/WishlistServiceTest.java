package ru.cohenrol.profile.domain.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cohenrol.profile.datasource.model.WishlistEntity;
import ru.cohenrol.profile.datasource.model.WishlistSettingsEntity;
import ru.cohenrol.profile.datasource.repository.WishlistRepository;
import ru.cohenrol.profile.domain.exception.inner.NotWishlistOwnerException;
import ru.cohenrol.profile.domain.exception.inner.WishlistNotFoundException;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.model.WishlistSettingsUpdateRequest;
import ru.cohenrol.profile.domain.model.WishlistUpdateRequest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private DomainMapper domainMapper;

    @InjectMocks
    private WishlistService wishlistService;

    @Nested
    class CreateTests {
        @Test
        void shouldCreateWishlistWithSettingsAndTimestamps() {
            UUID userId = UUID.randomUUID();
            WishlistEntity wishlist = new WishlistEntity();
            when(wishlistRepository.save(any(WishlistEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            WishlistEntity result = wishlistService.create(userId, wishlist);

            assertNotNull(result);
            assertEquals(userId, result.getUserId());
            assertNotNull(result.getWishlistId());
            assertNotNull(result.getCreatedAt());
            assertNotNull(result.getSettings());
            assertEquals(wishlist, result.getSettings().getWishlist());
            verify(wishlistRepository, times(1)).save(wishlist);
        }
    }

    @Nested
    class GetWishlistByIdTests {
        @Test
        void shouldReturnWishlistWhenExists() {
            UUID wishlistId = UUID.randomUUID();
            WishlistEntity expected = new WishlistEntity();
            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.of(expected));

            WishlistEntity result = wishlistService.getWishlistById(wishlistId);

            assertEquals(expected, result);
        }

        @Test
        void shouldThrowWishlistNotFoundExceptionWhenNotFound() {
            UUID wishlistId = UUID.randomUUID();
            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.empty());

            assertThrows(WishlistNotFoundException.class,
                () -> wishlistService.getWishlistById(wishlistId));
        }
    }

    @Nested
    class OwnerValidationTests {
        @Test
        void shouldPassValidationWhenUserIsOwner() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            WishlistEntity wishlist = new WishlistEntity();
            wishlist.setUserId(userId);
            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.of(wishlist));

            assertDoesNotThrow(() -> wishlistService.validateOwner(userId, wishlistId));
        }

        @Test
        void shouldThrowNotWishlistOwnerExceptionWhenUserIsNotOwner() {
            UUID userId = UUID.randomUUID();
            UUID wrongUserId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            WishlistEntity wishlist = new WishlistEntity();
            wishlist.setUserId(wrongUserId);
            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.of(wishlist));

            assertThrows(NotWishlistOwnerException.class,
                () -> wishlistService.validateOwner(userId, wishlistId));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void shouldDeleteWishlistWhenUserIsOwner() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            WishlistEntity wishlist = new WishlistEntity();
            wishlist.setUserId(userId);
            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.of(wishlist));

            wishlistService.deleteWishlist(userId, wishlistId);

            verify(wishlistRepository, times(1)).delete(wishlist);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void shouldUpdateWishlistFields() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            WishlistEntity wishlist = new WishlistEntity();
            wishlist.setUserId(userId);
            WishlistUpdateRequest request = new WishlistUpdateRequest();

            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.of(wishlist));

            wishlistService.updateWishlist(userId, wishlistId, request);

            verify(domainMapper, times(1)).updateWishlistFromDto(request, wishlist);
            verify(wishlistRepository, times(1)).save(wishlist);
        }
    }

    @Nested
    class SettingsTests {
        @Test
        void shouldReturnSettings() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            WishlistEntity wishlist = new WishlistEntity();
            wishlist.setUserId(userId);
            WishlistSettingsEntity settings = new WishlistSettingsEntity();
            wishlist.setSettings(settings);

            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.of(wishlist));

            WishlistSettingsEntity result = wishlistService.getSettings(userId, wishlistId);

            assertEquals(settings, result);
        }

        @Test
        void shouldUpdateSettings() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            WishlistEntity wishlist = new WishlistEntity();
            wishlist.setUserId(userId);
            WishlistSettingsEntity settings = new WishlistSettingsEntity();
            wishlist.setSettings(settings);
            WishlistSettingsUpdateRequest request = new WishlistSettingsUpdateRequest();

            when(wishlistRepository.findByWishlistId(wishlistId)).thenReturn(Optional.of(wishlist));

            wishlistService.updateSettings(userId, wishlistId, request);

            verify(domainMapper, times(1)).updateSettingsFromDto(request, settings);
            verify(wishlistRepository, times(1)).save(wishlist);
        }
    }
}
