package ru.cohenrol.profile.domain.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemUpdateRequest {
    private String title;
    private String description;
    private String link;
    private BigDecimal price;
    private String imageUrl;
}