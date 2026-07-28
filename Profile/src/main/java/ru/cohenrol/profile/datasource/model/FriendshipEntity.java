package ru.cohenrol.profile.datasource.model;

import jakarta.persistence.*;
import lombok.*;
import ru.cohenrol.profile.datasource.enums.FriendshipStatus;
import java.time.Instant;

@Entity
@Table(name = "friendships", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FriendshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private UserEntity friend;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FriendshipStatus status = FriendshipStatus.PENDING;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}