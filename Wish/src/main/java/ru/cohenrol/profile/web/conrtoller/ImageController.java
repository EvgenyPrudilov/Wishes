package ru.cohenrol.profile.web.conrtoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.profile.domain.ServicesGate;
import ru.cohenrol.profile.web.model.GetItemResponseDto;
import ru.cohenrol.profile.web.model.UploadImageRequestDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private")
@RequiredArgsConstructor
public class ImageController {
    private final ServicesGate servicesGate;

    @PostMapping("/{wishlistId}/item/{itemId}/image")
//    @RequestParam("file") указывает Spring Boot, что файл нужно достать из тела HTTP-запроса из поля с именем file. Это стандартный способ передачи файлов через формы (multipart/form-data). Она автоматически связывает загруженный клиентом файл с переменной MultipartFile file в методе.
    public ResponseEntity<Void> uploadImage(
        @AuthenticationPrincipal UUID authorizedUserId,
        @PathVariable UUID wishlistId,
        @PathVariable UUID itemId,
        @Valid UploadImageRequestDto imageRequestDto
    ) {
        servicesGate.uploadImage(authorizedUserId, wishlistId, itemId, imageRequestDto);
        return ResponseEntity.noContent().build();
    }
}