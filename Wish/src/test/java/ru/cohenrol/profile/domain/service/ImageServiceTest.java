package ru.cohenrol.profile.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.cohenrol.profile.datasource.model.ItemEntity;
import ru.cohenrol.profile.datasource.repository.ItemRepository;
import ru.cohenrol.profile.domain.exception.inner.ImageEmptyException;
import ru.cohenrol.profile.domain.exception.inner.ImageUploadProcessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    void setUp() {
        String dummyUploadDir = "target/test-uploads";
        ReflectionTestUtils.setField(imageService, "uploadDir", dummyUploadDir);
    }

    @Nested
    class UploadImageTests {

        @Test
        void shouldThrowImageEmptyExceptionWhenFileIsNull() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();

            assertThrows(ImageEmptyException.class,
                () -> imageService.uploadImage(userId, wishlistId, itemId, null));
        }

        @Test
        void shouldThrowImageEmptyExceptionWhenFileIsEmpty() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            MultipartFile emptyFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[0]);

            assertThrows(ImageEmptyException.class,
                () -> imageService.uploadImage(userId, wishlistId, itemId, emptyFile));
        }

        @Test
        void shouldThrowExceptionWhenItemValidationFails() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());

            when(itemService.getAndValidateItem(wishlistId, itemId)).thenThrow(new RuntimeException("Item error"));

            assertThrows(RuntimeException.class,
                () -> imageService.uploadImage(userId, wishlistId, itemId, file));
        }

        @Test
        void shouldUploadImageSuccessfully() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", "some-image-data".getBytes());

            ItemEntity item = new ItemEntity();
            item.setItemId(itemId);
            when(itemService.getAndValidateItem(wishlistId, itemId)).thenReturn(item);

            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
                mockedFiles.when(() -> Files.write(any(Path.class), any(byte[].class))).thenReturn(null);

                assertDoesNotThrow(() -> imageService.uploadImage(userId, wishlistId, itemId, file));

                assertNotNull(item.getImageUrl());
                assertTrue(item.getImageUrl().startsWith("/media/"));
                assertTrue(item.getImageUrl().endsWith(".png"));

                verify(wishlistService, times(1)).validateOwner(userId, wishlistId);
                verify(itemRepository, times(1)).save(item);
            }
        }

        @Test
        void shouldThrowImageUploadProcessExceptionWhenIOExceptionOccurs() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            MockMultipartFile file = new MockMultipartFile("file", "photo.jpg", "image/jpeg", "data".getBytes());

            ItemEntity item = new ItemEntity();
            when(itemService.getAndValidateItem(wishlistId, itemId)).thenReturn(item);

            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
                mockedFiles.when(() -> Files.write(any(Path.class), any(byte[].class))).thenThrow(new IOException("Disk full"));

                assertThrows(ImageUploadProcessException.class,
                    () -> imageService.uploadImage(userId, wishlistId, itemId, file));

                verify(itemRepository, never()).save(any());
            }
        }

        @Test
        void shouldRollbackFileWhenRepositorySaveFails() {
            UUID userId = UUID.randomUUID();
            UUID wishlistId = UUID.randomUUID();
            UUID itemId = UUID.randomUUID();
            MockMultipartFile file = new MockMultipartFile("file", "photo.jpg", "image/jpeg", "data".getBytes());

            ItemEntity item = new ItemEntity();
            when(itemService.getAndValidateItem(wishlistId, itemId)).thenReturn(item);
            when(itemRepository.save(item)).thenThrow(new RuntimeException("Database down"));

            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
                mockedFiles.when(() -> Files.write(any(Path.class), any(byte[].class))).thenReturn(null);
                mockedFiles.when(() -> Files.deleteIfExists(any(Path.class))).thenReturn(true);

                assertThrows(RuntimeException.class,
                    () -> imageService.uploadImage(userId, wishlistId, itemId, file));

                mockedFiles.verify(() -> Files.deleteIfExists(any(Path.class)), times(1));
            }
        }
    }
}
