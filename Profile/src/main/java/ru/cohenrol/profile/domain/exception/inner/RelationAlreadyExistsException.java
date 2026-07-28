package ru.cohenrol.profile.domain.exception.inner;

import org.springframework.http.HttpStatus;
import ru.cohenrol.profile.datasource.enums.FriendshipStatus;

public final class RelationAlreadyExistsException extends ProfileException {
    public RelationAlreadyExistsException(FriendshipStatus status) {
        super("Relation already exists with status: " + status + ".", HttpStatus.BAD_REQUEST);
    }
}
