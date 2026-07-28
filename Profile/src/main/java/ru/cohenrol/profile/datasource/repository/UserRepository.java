package ru.cohenrol.profile.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cohenrol.profile.datasource.model.UserEntity;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByName(String name);

    @Modifying
    @Query("UPDATE UserEntity u SET u.friendsCount = u.friendsCount + :delta WHERE u.id = :id")
    void updateFriendsCount(@Param("id") UUID id, @Param("delta") int delta);
}
