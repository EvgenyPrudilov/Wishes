package ru.cohenrol.profile.domain.mapper;


import org.mapstruct.*;
import ru.cohenrol.profile.datasource.model.ItemEntity;
import ru.cohenrol.profile.datasource.model.WishlistEntity;
import ru.cohenrol.profile.datasource.model.WishlistSettingsEntity;
import ru.cohenrol.profile.domain.model.ItemUpdateRequest;
import ru.cohenrol.profile.domain.model.WishlistSettingsUpdateRequest;
import ru.cohenrol.profile.domain.model.WishlistUpdateRequest;
import ru.cohenrol.profile.web.model.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = { LocalDateTime.class })
public interface DomainMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wishlistId", ignore = true) // Оставляем генерацию UUID.randomUUID()
    @Mapping(target = "userId", ignore = true)     // Задается отдельно в сервисе из аргумента метода
    @Mapping(target = "createdAt", ignore = true)  // Оставляем генерацию Instant.now()
    @Mapping(target = "settings", ignore = true)   // Инициализируется вручную в сервисе
    @Mapping(target = "itemEntities", ignore = true) // Оставляем пустой ArrayList
    WishlistEntity toWishlistEntity(WishlistCreateRequestDto dto);

    WishlistUpdateRequest toWishlistUpdateRequest(WishlistUpdateRequestDto dto);

    WishlistCreateResponseDto toWishlistCreateResponseDto(WishlistEntity entity);

    GetWishlistResponseDto toGetWishlistResponseDto(WishlistEntity wishlistById);

    WishlistSettingsResponseDto toWishlistSettingsResponseDto(WishlistSettingsEntity wishlistSettingsEntity);
    WishlistSettingsUpdateRequest toWishlistSettingsUpdateRequest(WishlistSettingsUpdateRequestDto wishlistSettingsUpdateRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wishlistEntity", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSettingsFromDto(WishlistSettingsUpdateRequest dto, @MappingTarget WishlistSettingsEntity settings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wishlistId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "itemEntities", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWishlistFromDto(WishlistUpdateRequest request, @MappingTarget WishlistEntity wishlist);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemId", ignore = true) // Генерируется дефолтным UUID.randomUUID()
    @Mapping(target = "wishlistEntity", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ItemEntity toItemEntity(ItemCreateRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "wishlistEntity", ignore = true)
    @Mapping(target = "reserved", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemUpdateRequest dto, @MappingTarget ItemEntity item);

    ItemUpdateRequest toItemUpdateRequest(ItemUpdateRequestDto itemUpdateRequestDto);

    GetItemResponseDto toGetItemResponseDto(ItemEntity itemEntity);
}
