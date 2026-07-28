package ru.cohenrol.profile.web.mapper;


import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.cohenrol.profile.datasource.model.UserEntity;
import ru.cohenrol.profile.domain.model.UserProfile;
import ru.cohenrol.profile.domain.model.UserProfileWithFriends;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;
import ru.cohenrol.profile.web.model.dto.UserProfileWithFriendsResponseDto;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;

@Mapper(componentModel = "spring")
public interface WebMapper {
    UserProfileResponseDto toShortDto(UserEntity userEntity);
    UserProfileWithFriendsResponseDto toDto(UserProfileWithFriends userProfileWithFriends);
    UserProfileResponseDto toDto(UserProfile userProfile);

    default Page<UserProfileResponseDto> toDto(Page<UserEntity> userEntityPage) {
        return userEntityPage.map(this::toShortDto);
    }
    // Метод для маппинга страниц, который MapStruct вызовет под капотом
    default Page<UserProfileResponseDto> mapPage(Page<UserEntity> friends) {
        if (friends == null) {
            return null;
        }
        return friends.map(this::toShortDto);
    }
}
