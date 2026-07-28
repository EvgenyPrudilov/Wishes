package ru.cohenrol.profile.web.conrtoller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cohenrol.profile.domain.ServicesGate;
import ru.cohenrol.profile.web.model.dto.UserProfileWithFriendsResponseDto;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
public class InternalProfileController {
    private final ServicesGate servicesGate;


//    @GetMapping("/check-frienship")
//    public ResponseEntity<Boolean> checkFriendship(
//        @RequestParam UUID userId,
//        @RequestParam UUID friendId
//    ) {
//        boolean areFriends = servicesGate.checkFriendship(userId, friendId);
//        return ResponseEntity.ok(areFriends);
//    }

}