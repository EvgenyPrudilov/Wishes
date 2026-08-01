package ru.cohenrol.profile.web.model.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Data
public class UserProfileResponseDto {
    private UUID userId;
    private String name;
    private int friendsCount;
}