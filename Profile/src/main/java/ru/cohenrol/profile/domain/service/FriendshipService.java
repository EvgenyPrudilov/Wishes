package ru.cohenrol.profile.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.profile.datasource.enums.FriendshipStatus;
import ru.cohenrol.profile.datasource.model.FriendshipEntity;
import ru.cohenrol.profile.datasource.model.UserEntity;
import ru.cohenrol.profile.datasource.repository.FriendshipRepository;
import ru.cohenrol.profile.datasource.repository.UserRepository;
import ru.cohenrol.profile.domain.exception.inner.*;
import ru.cohenrol.profile.domain.mapper.DomainMapper;
import ru.cohenrol.profile.domain.model.UserProfile;
import ru.cohenrol.profile.domain.model.UserProfileWithFriends;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final DomainMapper domainMapper;

    @Transactional(readOnly = true)
    public Page<UserEntity> getUserFriends(UUID userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return friendshipRepository.findFriendsByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public boolean checkFriendship(UUID userId, UUID friendId) {
        return friendshipRepository.areFriends(userId, friendId);
    }

    @Transactional
    public void sendFriendRequest(UUID requesterId, UUID targetId) {
        if (requesterId.equals(targetId)) {
            throw new IllegalArgumentException("You cannot add yourself as a friend");
        }

        UserEntity requester = userRepository.findById(requesterId)
            .orElseThrow(() -> new UserNotFoundException(requesterId));
        UserEntity target = userRepository.findById(targetId)
            .orElseThrow(() -> new UserNotFoundException(targetId));

        friendshipRepository.findRelation(requesterId, targetId).ifPresent(f -> {
            throw new RelationAlreadyExistsException(f.getStatus());
        });

        friendshipRepository.save(
            FriendshipEntity.builder()
                .user(requester)
                .friend(target)
                .status(FriendshipStatus.PENDING)
                .build()
        );
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getIncomingRequests(UUID userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return friendshipRepository.findIncomingRequests(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> getOutgoingRequests(UUID userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return friendshipRepository.findOutgoingRequests(userId, pageable);
    }

    @Transactional
    public void acceptFriendRequest(UUID currentUserId, UUID requesterId) {
        FriendshipEntity relation = friendshipRepository.findRelation(currentUserId, requesterId)
            .orElseThrow(() -> new FriendRequestNotFoundException(requesterId));

        if (!relation.getFriend().getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("Only the recipient can accept a friend request");
        }

        if (relation.getStatus() == FriendshipStatus.ACCEPTED) {
            throw new AlreadyFriendsException();
        }

        relation.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(relation);

        userRepository.updateFriendsCount(relation.getUser().getUserId(), 1);
        userRepository.updateFriendsCount(relation.getFriend().getUserId(), 1);
    }

    @Transactional
    public void removeFriend(UUID currentUserId, UUID targetId) {
        FriendshipEntity relation = friendshipRepository.findRelation(currentUserId, targetId)
            .orElseThrow(() -> new FriendshipNotFoundException(currentUserId, targetId));

        // Проверяем, что они действительно друзья, иначе это не операция "удаления друга"
        if (relation.getStatus() != FriendshipStatus.ACCEPTED) {
            throw new FriendshipNotFoundException(currentUserId, targetId);
        }

        userRepository.updateFriendsCount(relation.getUser().getUserId(), -1);
        userRepository.updateFriendsCount(relation.getFriend().getUserId(), -1);
        friendshipRepository.delete(relation);
    }

    @Transactional
    public void rejectFriendRequest(UUID currentUserId, UUID requesterId) {
        FriendshipEntity relation = friendshipRepository.findPendingRequest(requesterId, currentUserId)
            .orElseThrow(() -> new FriendRequestNotFoundException(currentUserId));
        friendshipRepository.delete(relation);
    }
}