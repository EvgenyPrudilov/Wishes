package ru.cohenrol.authserver.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cohenrol.authserver.broker.logging.events.RegisterationLogEvent;
import ru.cohenrol.authserver.datasource.repository.CustomUserRepository;
import ru.cohenrol.authserver.domain.exception.UsernameAlreadyExistsException;
import ru.cohenrol.authserver.domain.model.RegisterRequest;
import ru.cohenrol.authserver.domain.model.User;
import ru.cohenrol.authserver.web.mapper.WebMappers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final CustomUserRepository customUserRepository;
    private final PasswordEncoder passwordEncoder;
//    private final KafkaLogProducerService kafkaLogProducerService;

    @Transactional
    public User registerNewUser(RegisterRequest request) {
        String userName = request.getUsername();
        String email = request.getEmail();

        if (customUserRepository.existsByUsername(userName)) {
            throw new UsernameAlreadyExistsException(userName);
        }
        if (customUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(email);
        }

        User user = new User(userName, email);
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        user.setEnabled(true);
        user.setUuid(UUID.randomUUID());

        // sendVerificationEmail(savedUser);
        User registeredUser = customUserRepository.save(user);

//        kafkaLogProducerService.send(
//            new RegisterationLogEvent(LogLevel.INFO, "User " + userName + " has been registered.", registeredUser)
//        );
        return registeredUser;
    }
}