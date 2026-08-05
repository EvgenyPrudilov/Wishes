package ru.cohenrol.profile.domain.exception.inner;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public sealed abstract class WishlistException
    extends RuntimeException
    permits NotFriendException, NotWishlistOwnerException, WishlistNotFoundException
{
    @Getter
    final private HttpStatusCode statusCode;
//    @Getter
//    final private String serviceName;

    public WishlistException(String cause, HttpStatusCode statusCode) {
        super(cause);
        this.statusCode = statusCode;
//        this.serviceName = serviceName;
    }
}
