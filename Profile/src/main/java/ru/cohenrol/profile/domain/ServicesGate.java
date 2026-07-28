package ru.cohenrol.profile.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.profile.datasource.model.FriendshipEntity;
import ru.cohenrol.profile.domain.exception.inner.FriendRequestNotFoundException;
import ru.cohenrol.profile.domain.service.FriendshipService;
import ru.cohenrol.profile.domain.service.ProfileService;
import ru.cohenrol.profile.web.mapper.WebMapper;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;
import ru.cohenrol.profile.web.model.dto.UserProfileWithFriendsResponseDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicesGate {
    private final ProfileService profileService;
    private final FriendshipService friendshipService;
    private final WebMapper webMapper;

    public UserProfileResponseDto getUserProfile(UUID userId) {
        return webMapper.toDto(
            profileService.getUserProfile(userId)
        );
    }

    public UserProfileWithFriendsResponseDto getMyProfileWithFriends(UUID currentUserId, Pageable pageable) {
        return webMapper.toDto(
            profileService.getMyProfileWithFriends(currentUserId, pageable)
        );
    }

    public UserProfileResponseDto getMyProfile(UUID currentUserId) {
        return webMapper.toDto(
            profileService.getUserProfile(currentUserId)
        );
    }

    public Page<UserProfileResponseDto> getMyFriends(UUID userId, Pageable pageable) {
        return webMapper.toDto(
            friendshipService.getUserFriends(userId, pageable)
        );
    }

    public boolean checkFriendship(UUID userId, UUID friendId) {
        return friendshipService.checkFriendship(userId, friendId);
    }

    public void sendFriendRequest(UUID requesterId, UUID targetId) {
        friendshipService.sendFriendRequest(requesterId, targetId);
    }

    public void acceptFriendRequest(UUID currentUserId, UUID requesterId) {
        friendshipService.acceptFriendRequest(currentUserId, requesterId);
    }

    public void removeMyFriend(UUID currentUserId, UUID targetId) {
        friendshipService.removeFriend(currentUserId, targetId);
    }

    public Page<UserProfileResponseDto> getMyIncomingRequests(UUID currentUserId, Pageable pageable) {
        return webMapper.toDto(
            friendshipService.getIncomingRequests(currentUserId, pageable)
        );
    }

    public Page<UserProfileResponseDto> getMyOutgoingRequests(UUID currentUserId, Pageable pageable) {
        return webMapper.toDto(
            friendshipService.getOutgoingRequests(currentUserId, pageable)
        );
    }

    public void rejectFriendRequest(UUID currentUserId, UUID requesterId) {
        friendshipService.rejectFriendRequest(currentUserId, requesterId);
    }

    public UserProfileResponseDto getProfile(UUID currentUserId, UUID targetUserId) {
        if (currentUserId != null && currentUserId == targetUserId) {
            return this.getMyProfile(currentUserId);
        }
        return this.getUserProfile(targetUserId);
    }
}
