package ru.cohenrol.authserver.datasource.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cohenrol.authserver.datasource.mapper.DatasourceMapper;
import ru.cohenrol.authserver.domain.model.User;

import java.util.Optional;

@Component
public class CustomUserRepository {
    private final UserRepository userRepository;
    private final DatasourceMapper datasourceMapper;

    @Autowired
    public CustomUserRepository(UserRepository userRepository, DatasourceMapper datasourceMapper) {
        this.userRepository = userRepository;
        this.datasourceMapper = datasourceMapper;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(datasourceMapper::toDomain);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return datasourceMapper.toDomain(userRepository.save(datasourceMapper.toEntity(user)));
    }
}
