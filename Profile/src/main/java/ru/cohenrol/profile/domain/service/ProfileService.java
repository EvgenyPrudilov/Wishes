package ru.cohenrol.profile.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.profile.datasource.model.UserEntity;
import ru.cohenrol.profile.datasource.repository.UserRepository;
import ru.cohenrol.profile.domain.exception.inner.*;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.model.UserProfile;
import ru.cohenrol.profile.domain.model.UserProfileWithFriends;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final FriendshipService friendshipService;
    private final DomainMapper domainMapper;

    @Transactional(readOnly = true)
    public UserProfile getUserProfile(UUID currentUserId) {
        UserEntity user = userRepository.findById(currentUserId)
            .orElseThrow(() -> new UserNotFoundException(currentUserId));

        return UserProfile.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .friendsCount(user.getFriendsCount())
            .build();
    }

    @Transactional(readOnly = true)
    public UserProfileWithFriends getMyProfileWithFriends(UUID currentUserId, Pageable pageable) {
        UserProfileWithFriends profile = domainMapper.toWithFriends(getUserProfile(currentUserId));
        profile.setFriends(friendshipService.getUserFriends(currentUserId, pageable).map(domainMapper::toUserProfile));
        return profile;
    }
}