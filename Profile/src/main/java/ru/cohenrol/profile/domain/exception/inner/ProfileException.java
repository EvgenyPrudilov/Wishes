package ru.cohenrol.profile.domain.exception.inner;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public sealed abstract class ProfileException
    extends RuntimeException
    permits RelationAlreadyExistsException, UserNotFoundException,
            FriendRequestNotFoundException, AlreadyFriendsException,
            FriendshipNotFoundException
{
    @Getter
    final private HttpStatusCode statusCode;
//    @Getter
//    final private String serviceName;

    public ProfileException(String cause, HttpStatusCode statusCode) {
        super(cause);
        this.statusCode = statusCode;
//        this.serviceName = serviceName;
    }
}
