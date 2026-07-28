package ru.cohenrol.profile.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cohenrol.profile.datasource.model.WishlistEntity;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {
    // Поиск по UUID самого вишлиста
    Optional<WishlistEntity> findByWishlistId(UUID wishlistId);

    // Удаление по UUID самого вишлиста
    void deleteByWishlistId(UUID wishlistId);
}