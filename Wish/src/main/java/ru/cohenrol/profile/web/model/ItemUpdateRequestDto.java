package ru.cohenrol.profile.web.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemUpdateRequestDto {
    private String title;
    private String description;
    private String link;
    private BigDecimal price;
    private String imageUrl;
}