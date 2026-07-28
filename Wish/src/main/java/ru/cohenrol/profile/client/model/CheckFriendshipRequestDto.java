package ru.cohenrol.profile.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckFriendshipRequestDto {
    private UUID authorizedUser;
    private UUID wishlistOwner;
}