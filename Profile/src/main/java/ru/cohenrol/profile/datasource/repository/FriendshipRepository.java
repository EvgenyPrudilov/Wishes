package ru.cohenrol.profile.datasource.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cohenrol.profile.datasource.model.FriendshipEntity;
import ru.cohenrol.profile.datasource.model.UserEntity;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    // Поиск конкретной связи между двумя пользователями (в любом направлении)
    @Query("""
        SELECT f FROM FriendshipEntity f 
        WHERE (f.user.id = :userId AND f.friend.id = :friendId) 
           OR (f.user.id = :friendId AND f.friend.id = :userId)
    """)
    Optional<FriendshipEntity> findRelation(@Param("userId") UUID userId, @Param("friendId") UUID friendId);

    // Проверка, являются ли пользователи подтвержденными друзьями
    @Query("""
        SELECT COUNT(f) > 0 FROM FriendshipEntity f 
        WHERE f.status = 'ACCEPTED' AND 
        ((f.user.id = :userId AND f.friend.id = :friendId) OR 
         (f.user.id = :friendId AND f.friend.id = :userId))
    """)
    boolean areFriends(@Param("userId") UUID userId, @Param("friendId") UUID friendId);

    // Получение списка всех подтвержденных друзей пользователя с поддержкой пагинации
    @Query("""
        SELECT CASE WHEN f.user.id = :userId THEN f.friend ELSE f.user END 
        FROM FriendshipEntity f 
        WHERE f.status = 'ACCEPTED' AND (f.user.id = :userId OR f.friend.id = :userId)
    """)
    Page<UserEntity> findFriendsByUserId(@Param("userId") UUID userId, Pageable pageable);

    // Получить входящие заявки (кто-то хочет со мной подружиться)
    @Query("SELECT f.user FROM FriendshipEntity f WHERE f.friend.id = :userId AND f.status = 'PENDING'")
    Page<UserEntity> findIncomingRequests(@Param("userId") UUID userId, Pageable pageable);

    // Получить исходящие заявки (я отправил запросы другим)
    @Query("SELECT f.friend FROM FriendshipEntity f WHERE f.user.id = :userId AND f.status = 'PENDING'")
    Page<UserEntity> findOutgoingRequests(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
    SELECT f FROM FriendshipEntity f 
    WHERE f.user.id = :requesterId 
      AND f.friend.id = :recipientId 
      AND f.status = 'PENDING'
""")
    Optional<FriendshipEntity> findPendingRequest(
        @Param("requesterId") UUID requesterId,
        @Param("recipientId") UUID recipientId
    );
}