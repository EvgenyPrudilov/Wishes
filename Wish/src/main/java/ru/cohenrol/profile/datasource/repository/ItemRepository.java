package ru.cohenrol.profile.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cohenrol.profile.datasource.model.ItemEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    // Найти подарок по его UUID
    Optional<ItemEntity> findByItemId(UUID itemId);

    // Удалить подарок по его UUID
    void deleteByItemId(UUID itemId);
}