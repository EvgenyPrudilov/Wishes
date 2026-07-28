package ru.cohenrol.loggingservice.datasource.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.cohenrol.loggingservice.datasource.model.LogEntity;
import ru.cohenrol.loggingservice.logging.enums.LogLevel;
import ru.cohenrol.loggingservice.logging.enums.LogReason;

import java.time.Instant;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    @Query(
        "SELECT l FROM LogEntity l WHERE " +
            "(:service IS NULL OR l.serviceName = :service) AND " +
            "(:level IS NULL OR l.level = :level) AND " +
            "(:reason IS NULL OR l.reason = :reason) AND " +
            "(:user IS NULL OR l.userName = :user) AND " +
            "(cast(:from as timestamp) IS NULL OR l.timestamp >= :from) AND " +
            "(cast(:to as timestamp) IS NULL OR l.timestamp <= :to) " +
        "ORDER BY l.timestamp DESC"
    )
    List<LogEntity> findLogsByFilters(
        String service, LogLevel level, LogReason reason, String user, Instant from, Instant to
    );
}
