package ru.cohenrol.profile.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cohenrol.profile.datasource.model.WishlistSettingsEntity;

@Repository
public interface WishlistSettingsRepository extends JpaRepository<WishlistSettingsEntity, Long> {
    // Так как используется @MapsId, ID настроек совпадает с ID вишлиста
}