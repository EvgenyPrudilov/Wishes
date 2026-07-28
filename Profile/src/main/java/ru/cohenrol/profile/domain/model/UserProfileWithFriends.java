package ru.cohenrol.profile.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ru.cohenrol.profile.web.model.dto.UserProfileResponseDto;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileWithFriends {
    private UUID id;
    private String name;
    private int friendsCount;
    private Page<UserProfile> friends;
}