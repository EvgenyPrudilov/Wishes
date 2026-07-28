package ru.cohenrol.authserver.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cohenrol.authserver.datasource.model.ServiceEntity;
import ru.cohenrol.authserver.datasource.repository.ServiceRepository;
import ru.cohenrol.authserver.domain.token.JwtProvider;

@Service
@RequiredArgsConstructor
public class ServiceAuthService {
    private final ServiceRepository serviceRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public String authenticateService(String serviceName, String password) {
        ServiceEntity service = serviceRepository.findByServiceName(serviceName)
            .orElseThrow(() -> new RuntimeException("Микросервис не найден"));

        if (!passwordEncoder.matches(password, service.getPassword())) {
            throw new RuntimeException("Неверный секретный пароль сервиса");
        }

        return jwtProvider.generateToken(service);
    }
}