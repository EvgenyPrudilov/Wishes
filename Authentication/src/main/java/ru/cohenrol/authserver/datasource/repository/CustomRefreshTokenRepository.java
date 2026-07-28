package ru.cohenrol.authserver.datasource.repository;

import org.springframework.stereotype.Repository;
import ru.cohenrol.authserver.datasource.mapper.DatasourceMapper;
import ru.cohenrol.authserver.domain.model.RefreshToken;

import java.util.Optional;

@Repository
public class CustomRefreshTokenRepository {

    private final RefreshTokenRepository refreshTokenRepository;
    private final DatasourceMapper datasourceMapper;

    public CustomRefreshTokenRepository(
        RefreshTokenRepository refreshTokenRepository,
        DatasourceMapper datasourceMapper
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.datasourceMapper = datasourceMapper;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(datasourceMapper::toDomain);
    }


    public void flush() {
        refreshTokenRepository.flush();
    }

    public void deleteByUser_Id(Long userId) {
        refreshTokenRepository.deleteByUser_Id(userId);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return datasourceMapper.toDomain(
            refreshTokenRepository.save(datasourceMapper.toEntity(refreshToken))
        );
    }
}
