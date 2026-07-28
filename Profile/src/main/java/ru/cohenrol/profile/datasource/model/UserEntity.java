package ru.cohenrol.profile.datasource.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "friends_count", nullable = false)
    private int friendsCount = 0;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    // Связь со всеми записями дружбы, где данный пользователь является инициатором (user_id)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipEntity> sentFriendships = new ArrayList<>();

    // Связь со всеми записями дружбы, где данный пользователь является получателем (friend_id)
    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipEntity> receivedFriendships = new ArrayList<>();
}