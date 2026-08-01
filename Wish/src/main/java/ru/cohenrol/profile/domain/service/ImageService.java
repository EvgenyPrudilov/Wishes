package ru.cohenrol.profile.domain.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.cohenrol.profile.client.ProfileServiceClient;
import ru.cohenrol.profile.datasource.enums.BookingPermission;
import ru.cohenrol.profile.datasource.enums.ReservationVisibility;
import ru.cohenrol.profile.datasource.model.ItemEntity;
import ru.cohenrol.profile.datasource.model.WishlistEntity;
import ru.cohenrol.profile.datasource.model.WishlistSettingsEntity;
import ru.cohenrol.profile.datasource.repository.ItemRepository;
import ru.cohenrol.profile.domain.exception.inner.ImageEmptyException;
import ru.cohenrol.profile.domain.exception.inner.ImageUploadProcessException;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.model.ItemUpdateRequest;
import ru.cohenrol.profile.web.model.ItemCreateRequestDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@RefreshScope
public class ImageService {
    private final ItemRepository itemRepository;
    private final WishlistService wishlistService;
    private final DomainMapper domainMapper;
    private final ProfileServiceClient profileServiceClient;
    private final ItemService itemService;

    @Value("${app.image-upload-path}")
    private String uploadDir;

    @Transactional
    public void uploadImage(UUID authorizedUserId, UUID wishlistId, UUID itemId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ImageEmptyException();
        }

        ItemEntity itemEntity;
        try {
            itemEntity = itemService.getAndValidateItem(wishlistId, itemId);
            wishlistService.validateOwner(authorizedUserId, wishlistId);
        } catch (RuntimeException e) {
            // logging
            throw e;
        }

        String randomFileName;
        Path path;
        try {
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            randomFileName = UUID.randomUUID() + extension;
            path = Paths.get(uploadDir, randomFileName);

//            Директорию нужно создавать, потому что при первом запуске Docker-контейнера общая папка может быть пустой или не существовать внутри Java-контейнера. Метод Files.write выбросит ошибку NoSuchFileException, если попытается записать файл в несуществующий путь. Files.createDirectories гарантирует, что папка гарантированно существует перед сохранением.  Если директория уже существует, метод Files.createDirectories() просто ничего не сделает. Он не выдаст ошибку и не перезапишет существующую папку. Это безопасная операция «check-and-create» (проверить и создать, если отсутствует).
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new ImageUploadProcessException();
        }

        try {
            itemEntity.setImageUrl("/media/" + randomFileName);
            itemRepository.save(itemEntity);
        } catch (RuntimeException e) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException ioException) {
                //logging event
            }
            throw e;
        }
    }
}