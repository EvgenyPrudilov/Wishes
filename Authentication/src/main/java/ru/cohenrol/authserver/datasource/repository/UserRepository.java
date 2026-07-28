package ru.cohenrol.authserver.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cohenrol.authserver.datasource.model.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
//    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<UserEntity> findByUsername(String username);
}