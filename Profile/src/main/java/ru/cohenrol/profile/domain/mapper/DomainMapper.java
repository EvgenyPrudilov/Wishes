package ru.cohenrol.profile.domain.mapper;

import org.mapstruct.Mapper;
import ru.cohenrol.profile.datasource.model.UserEntity;
import ru.cohenrol.profile.domain.model.UserProfile;
import ru.cohenrol.profile.domain.model.UserProfileWithFriends;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;

@Mapper(componentModel = "spring")
public interface DomainMapper {
    UserProfile toUserProfile(UserEntity userEntity);
    UserProfileWithFriends toWithFriends(UserProfile userProfile);
//    UserProfileResponseDto toDto(UserProfile userProfile);
//
//    default Page<UserProfileResponseDto> toDto(Page<UserEntity> userEntityPage) {
//        return userEntityPage.map(this::toShortDto);
//    }
//    // Метод для маппинга страниц, который MapStruct вызовет под капотом
//    default Page<UserProfileResponseDto> mapPage(Page<UserEntity> friends) {
//        if (friends == null) {
//            return null;
//        }
//        return friends.map(this::toShortDto);
//    }
}