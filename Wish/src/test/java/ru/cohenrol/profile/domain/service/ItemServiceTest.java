package ru.cohenrol.profile.domain.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cohenrol.profile.client.ProfileServiceClient;
import ru.cohenrol.profile.datasource.enums.BookingPermission;
import ru.cohenrol.profile.datasource.enums.ReservationVisibility;
import ru.cohenrol.profile.datasource.model.ItemEntity;
import ru.cohenrol.profile.datasource.model.WishlistEntity;
import ru.cohenrol.profile.datasource.model.WishlistSettingsEntity;
import ru.cohenrol.profile.datasource.repository.ItemRepository;
import ru.cohenrol.profile.domain.exception.inner.*;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.model.ItemUpdateRequest;
import ru.cohenrol.profile.web.model.ItemCreateRequestDto;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private DomainMapper domainMapper;

    @Mock
    private ProfileServiceClient profileServiceClient;

    @InjectMocks
    private ItemService itemService;

    private WishlistEntity createMockWishlist(UUID wishlistId, UUID ownerId) {
        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setWishlistId(wishlistId);
        wishlist.setUserId(ownerId);

        WishlistSettingsEntity settings = new WishlistSettingsEntity();
        settings.setBookingPrivacy(BookingPermission.EVERYONE);
        settings.setVisibilityMode(ReservationVisibility.WITHOUT_NAMES);
        wishlist.setSettings(settings);

        return wishlist;
    }

    @Nested
    class GetAndValidateItemTests {
        @Test
        void shouldReturnItemWhenValid() {
            UUID wishlistId = UUID.randomUUID();
            UUID wishlistOwnerId = UUID.randomUUID();
            ItemEntity item = new ItemEntity();
            UUID itemId = UUID.randomUUID();
            item.setItemId(itemId);
            item.setWishlist(createMockWishlist(wishlistId, wishlistOwnerId));

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            ItemEntity result = itemService.getAndValidateItem(wishlistId, itemId);

            assertEquals(item, result);
            verifyNoMoreInteractions(wishlistService);
        }

        @Test
        void shouldThrowItemNotFoundException() {
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.empty());

            assertThrows(ItemNotFoundException.class, () -> itemService.getAndValidateItem(wishlistId, itemId));
            verifyNoMoreInteractions(wishlistService);
        }

        @Test
        void shouldThrowItemNotFromWishlistException() {
            UUID wishlistId = UUID.randomUUID();
            UUID wrongWishlistId = UUID.randomUUID();
            UUID wishlistOwnerId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            ItemEntity item = new ItemEntity();
            item.setItemId(itemId);
            item.setWishlist(createMockWishlist(wrongWishlistId, wishlistOwnerId));

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            assertThrows(ItemNotFromWishlistException.class, () -> itemService.getAndValidateItem(wishlistId, itemId));
            verifyNoMoreInteractions(wishlistService);
        }
    }

    @Nested
    class GetItemFromWishlistTests {
        @Test
        void shouldReturnItemAndValidateOwner() {
            UUID wishlistOwnerId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            ItemEntity item = new ItemEntity();
            item.setItemId(itemId);
            item.setWishlist(createMockWishlist(wishlistId, wishlistOwnerId));

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            ItemEntity result = itemService.getItemFromWishlist(wishlistOwnerId, wishlistId, itemId);

            assertEquals(item, result);
            verify(wishlistService, times(1)).validateOwner(wishlistOwnerId, wishlistId);
            verifyNoMoreInteractions(wishlistService);
        }
    }

    @Nested
    class AddItemTests {
        @Test
        void shouldAddItemSuccessfully() {
            UUID wishlistOwnerId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            ItemCreateRequestDto dto = new ItemCreateRequestDto();
            WishlistEntity wishlist = createMockWishlist(wishlistId, wishlistOwnerId);
            ItemEntity item = new ItemEntity();

            when(wishlistService.getAndValidateOwner(wishlistOwnerId, wishlistId)).thenReturn(wishlist);
            when(domainMapper.toItemEntity(dto)).thenReturn(item);

            itemService.addItem(wishlistOwnerId, wishlistId, dto);

            assertEquals(wishlist, item.getWishlist());
            verify(itemRepository, times(1)).save(item);
            verifyNoMoreInteractions(wishlistService);
        }
    }

    @Nested
    class DeleteItemTests {
        @Test
        void shouldDeleteItemWhenValid() {
            UUID wishlistOwnerId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            ItemEntity item = new ItemEntity();
            item.setItemId(itemId);
            item.setWishlist(createMockWishlist(wishlistId, wishlistOwnerId));

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            itemService.deleteItem(wishlistOwnerId, wishlistId, itemId);

            verify(wishlistService, times(1)).validateOwner(wishlistOwnerId, wishlistId);
            verify(itemRepository, times(1)).delete(item);
            verifyNoMoreInteractions(wishlistService);
        }
    }

    @Nested
    class UpdateItemTests {
        @Test
        void shouldUpdateItemSuccessfully() {
            UUID wishlistOwnerId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            ItemUpdateRequest request = new ItemUpdateRequest();

            ItemEntity item = new ItemEntity();
            item.setItemId(itemId);
            item.setWishlist(createMockWishlist(wishlistId, wishlistOwnerId));

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            itemService.updateItem(wishlistOwnerId, wishlistId, itemId, request);

            verify(wishlistService, times(1)).validateOwner(wishlistOwnerId, wishlistId);
            verify(domainMapper, times(1)).updateItemFromDto(request, item);
            verify(itemRepository, times(1)).save(item);
            verifyNoMoreInteractions(wishlistService);
        }
    }

    @Nested
    class ReserveItemTests {
        @Test
        void shouldThrowExceptionWhenAlreadyReserved() {
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            ItemEntity item = new ItemEntity();
            item.setItemId(itemId);
            item.setReserved(true);
            item.setWishlist(createMockWishlist(wishlistId, UUID.randomUUID()));

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            assertThrows(ItemAlreadyReservedException.class,
                () -> itemService.reserveItem(UUID.randomUUID(), wishlistId, itemId, "User"));
        }

        @Test
        void shouldThrowExceptionWhenNotFriendAndPrivacyIsFriends() {
            UUID userId = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            WishlistEntity wishlist = createMockWishlist(wishlistId, ownerId);
            wishlist.getSettings().setBookingPrivacy(BookingPermission.FRIENDS);

            ItemEntity item = new ItemEntity();
            item.setItemId(itemId);
            item.setWishlist(wishlist);
            item.setReserved(false);

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));
            when(profileServiceClient.checkFriendship(userId, ownerId)).thenReturn(false);

            assertThrows(RuntimeException.class,
                () -> itemService.reserveItem(userId, wishlistId, itemId, "User"));
        }

        @Test
        void shouldThrowExceptionWhenWithNamesAndReservedByNameIsNull() {
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            WishlistEntity wishlist = createMockWishlist(wishlistId, UUID.randomUUID());
            wishlist.getSettings().setVisibilityMode(ReservationVisibility.WITH_NAMES);

            ItemEntity item = new ItemEntity();
            item.setWishlist(wishlist);
            item.setReserved(false);

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            assertThrows(RuntimeException.class,
                () -> itemService.reserveItem(UUID.randomUUID(), wishlistId, itemId, null));
        }

        @Test
        void shouldReserveSuccessfullyWhenConditionsMet() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            ItemEntity item = new ItemEntity();
            item.setWishlist(createMockWishlist(wishlistId, UUID.randomUUID()));
            item.setReserved(false);

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            itemService.reserveItem(userId, wishlistId, itemId, "John Doe");

            assertTrue(item.isReserved());
            assertEquals(userId, item.getReservedBy());
            assertEquals("John Doe", item.getReservedBy_Name());
        }
    }

    @Nested
    class UnreserveItemTests {
        @Test
        void shouldThrowExceptionWhenNotReserved() {
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            ItemEntity item = new ItemEntity();
            item.setReserved(false);
            item.setWishlist(createMockWishlist(wishlistId, UUID.randomUUID()));

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            assertThrows(ItemNotReservedException.class,
                () -> itemService.unreserveItemAsAuthorized(UUID.randomUUID(), wishlistId, itemId));
        }

        @Test
        void shouldThrowNotFriendExceptionWhenUnreservingSomeoneElseItem() {
            UUID userId = UUID.randomUUID();
            UUID alternativeUserId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            WishlistEntity wishlist = createMockWishlist(wishlistId, UUID.randomUUID());
            wishlist.getSettings().setBookingPrivacy(BookingPermission.FRIENDS);

            ItemEntity item = new ItemEntity();
            item.setWishlist(wishlist);
            item.setReserved(true);
            item.setReservedBy(alternativeUserId);

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            assertThrows(NotFriendException.class,
                () -> itemService.unreserveItemAsAuthorized(userId, wishlistId, itemId));
        }

        @Test
        void shouldUnreserveSuccessfullyAsAuthorized() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            ItemEntity item = new ItemEntity();
            item.setWishlist(createMockWishlist(wishlistId, UUID.randomUUID()));
            item.setReserved(true);
            item.setReservedBy(userId);
            item.setReservedBy_Name("John Doe");

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            itemService.unreserveItemAsAuthorized(userId, wishlistId, itemId);

            assertFalse(item.isReserved());
            assertNull(item.getReservedBy());
            assertNull(item.getReservedBy_Name());
        }

        @Test
        void shouldUnreserveSuccessfullyAsGuest() {
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            ItemEntity item = new ItemEntity();
            WishlistEntity wishlist = createMockWishlist(wishlistId, UUID.randomUUID());
            wishlist.getSettings().setBookingPrivacy(BookingPermission.EVERYONE);

            item.setWishlist(wishlist);
            item.setReserved(true);
            item.setReservedBy(null);
            item.setReservedBy_Name("Guest");

            when(itemRepository.findByItemId(itemId)).thenReturn(Optional.of(item));

            itemService.unreserveItemAsGuest(wishlistId, itemId);

            assertFalse(item.isReserved());
            assertNull(item.getReservedBy_Name());
        }
    }
}