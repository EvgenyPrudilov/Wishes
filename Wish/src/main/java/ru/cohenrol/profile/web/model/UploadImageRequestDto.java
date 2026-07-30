package ru.cohenrol.profile.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadImageRequestDto {
    @NotNull(message = "File must not be empty")
    private MultipartFile file;
}