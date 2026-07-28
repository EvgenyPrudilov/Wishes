package ru.cohenrol.profile.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.cohenrol.profile.domain.exception.external.ProfileServiceRequestException;
import ru.cohenrol.profile.client.model.CheckFriendshipRequestDto;
import ru.cohenrol.profile.client.model.CheckFriendshipResponseDto;

import java.util.UUID;

@Component
public class ProfileServiceClient {
    private final RestClient client;
    private final ServiceTokenManager serviceTokenManager;

    public ProfileServiceClient(
        @Qualifier("profileRestClient") RestClient client,
        ServiceTokenManager serviceTokenManager
    ) {
        this.client = client;
        this.serviceTokenManager = serviceTokenManager;
    }

    @Value("${spring.application.name}")
    private String serviceName;
    @Value("${services.profile.endpoints.check-friendship}")
    private String checkFriendshipUri;

    public boolean checkFriendship(UUID authorizedUser, UUID wishlistOwner) {
        CheckFriendshipResponseDto response = client.post()
            .uri(checkFriendshipUri)
            .header("Authorization", "Bearer " + serviceTokenManager.getAccessToken())
            .body(new CheckFriendshipRequestDto(authorizedUser, wishlistOwner))
            .retrieve()
            .onStatus(HttpStatusCode::isError, (req, res) -> {
                throw new ProfileServiceRequestException(res.getStatusCode());
            })
            .body(CheckFriendshipResponseDto.class); // Синхронно читает тело ответа

        return response != null && response.isFriends();
    }
}