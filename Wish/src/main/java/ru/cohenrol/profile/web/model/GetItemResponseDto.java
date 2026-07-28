package ru.cohenrol.profile.web.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GetItemResponseDto {
//    private UUID itemId; // Предполагается добавление UUID для ItemEntity по аналогии с вишлистом
    private String title;
    private String description;
    private String link;
    private BigDecimal price;
    private String imageUrl;
    private boolean reserved;
}