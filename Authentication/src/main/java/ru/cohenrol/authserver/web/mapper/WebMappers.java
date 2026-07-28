package ru.cohenrol.authserver.web.mapper;

import org.mapstruct.Mapper;
import ru.cohenrol.authserver.domain.model.*;
import ru.cohenrol.authserver.web.model.dto.*;

@Mapper(componentModel = "spring")
public interface WebMappers {
    UserDto toDto(User user);
    LoginRequest toDomain(LoginRequestDto dto);
    RegisterRequest toDomain(RegisterRequestDto dto);
    TokenResponseDto toDto(LoginResponseDto loginResponseDto);

    JwkKeyDto toDto(JwkKey key);
    JwtSetResponseDto toDto(JwtSet jwtSetResponse);

    RefreshResponseDto toDto(RefreshResponse refreshResponse);
    LoginResponseDto toDto(LoginResponse loginResponse);
}
