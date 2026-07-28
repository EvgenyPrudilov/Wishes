package ru.cohenrol.authserver.datasource.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cohenrol.authserver.datasource.model.ServiceEntity;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    Optional<ServiceEntity> findByServiceName(String serviceName);
}