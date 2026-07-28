package ru.cohenrol.profile.web.conrtoller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.profile.domain.ServicesGate;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;
import ru.cohenrol.profile.web.model.dto.UserProfileWithFriendsResponseDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/private")
@RequiredArgsConstructor
public class PrivateProfileController {
    private final ServicesGate servicesGate;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(
        @AuthenticationPrincipal UUID currentUserId
    ) {
        UserProfileResponseDto profile = servicesGate.getMyProfile(currentUserId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profile-with-friends")
    public ResponseEntity<UserProfileWithFriendsResponseDto> getMyProfileWithFriends(
        @AuthenticationPrincipal UUID currentUserId,
        @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable 
   ) {
        UserProfileWithFriendsResponseDto profile = servicesGate.getMyProfileWithFriends(currentUserId, pageable);
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/friends")
    public ResponseEntity<Page<UserProfileResponseDto>> getMyFriends(
        @AuthenticationPrincipal UUID currentUserId,
        @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable 
   ) {
        Page<UserProfileResponseDto> friends = servicesGate.getMyFriends(currentUserId, pageable);
        return ResponseEntity.ok(friends);
    }    

    @GetMapping("/incoming-requests")
    public ResponseEntity<Page<UserProfileResponseDto>> getMyIncomingRequests(
        @AuthenticationPrincipal UUID currentUserId,
        @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<UserProfileResponseDto> requests = servicesGate.getMyIncomingRequests(currentUserId, pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/outgoing-requests")
    public ResponseEntity<Page<UserProfileResponseDto>> getMyOutgoingRequests(
        @AuthenticationPrincipal UUID currentUserId,
        @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<UserProfileResponseDto> requests = servicesGate.getMyOutgoingRequests(currentUserId, pageable);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/outgoing-requests/{targetUserId}")
    public ResponseEntity<Void> sendFriendRequest(
        @AuthenticationPrincipal UUID currentUserId,
        @NonNull @PathVariable UUID targetUserId
    ) {
        servicesGate.sendFriendRequest(currentUserId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/incoming-requests/{requesterUserId}")
    public ResponseEntity<Void> acceptFriendRequest(
        @AuthenticationPrincipal UUID currentUserId,
        @NonNull @PathVariable UUID requesterUserId
    ) {
        servicesGate.acceptFriendRequest(currentUserId, requesterUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/incoming-requests/{requesterUserId}")
    public ResponseEntity<Void> rejectFriendRequest(
        @AuthenticationPrincipal UUID currentUserId,
        @NonNull @PathVariable UUID requesterUserId
    ) {
        servicesGate.rejectFriendRequest(currentUserId, requesterUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<Void> removeMyFriend(
        @AuthenticationPrincipal UUID currentUserId,
        @NonNull @PathVariable UUID targetUserId
    ) {
        servicesGate.removeMyFriend(currentUserId, targetUserId);
        return ResponseEntity.noContent().build();
    }
    
}