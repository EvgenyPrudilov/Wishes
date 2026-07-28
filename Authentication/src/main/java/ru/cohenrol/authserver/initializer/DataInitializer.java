package ru.cohenrol.authserver.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.cohenrol.authserver.datasource.model.ServiceEntity;
import ru.cohenrol.authserver.datasource.repository.ServiceRepository;

@Configuration
@RequiredArgsConstructor
//@RefreshScope
public class DataInitializer {
//    private final ServiceRepository serviceRepository;
//    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @Value("${app.jwt.services.enrollment.name}")
//    private String configServiceName;
//    @Value("${app.jwt.services.enrollment.password}")
//    private String configPassword;
//
//    @Bean
//    public CommandLineRunner initEnrollmentServiceCredentials() {
//        return args -> {
//            if (serviceRepository.findByServiceName(configServiceName).isEmpty()) {
//                ServiceEntity enrollmentService = new ServiceEntity(configServiceName, passwordEncoder.encode(configPassword));
//                serviceRepository.save(enrollmentService);
//
//                System.out.println(">>> Учетные данные для '" + configServiceName + "' успешно добавлены в PostgreSQL.");
//            } else {
//                System.out.println(">>> Сервис '" + configServiceName + "' уже присутствует в БД. Пропуск инициализации.");
//            }
//        };
//    }
}