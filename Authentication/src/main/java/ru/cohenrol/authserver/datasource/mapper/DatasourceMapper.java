package ru.cohenrol.authserver.datasource.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.cohenrol.authserver.datasource.model.RefreshTokenEntity;
import ru.cohenrol.authserver.datasource.model.UserEntity;
import ru.cohenrol.authserver.domain.model.RefreshToken;
import ru.cohenrol.authserver.domain.model.User;

@Mapper(componentModel = "spring")
public interface DatasourceMapper {

    User toDomain(UserEntity entity);
    UserEntity toEntity(User domain);

    RefreshToken toDomain(RefreshTokenEntity entity);
    @Mapping(target = "id", ignore = true)
    RefreshTokenEntity toEntity(RefreshToken domain);
}
