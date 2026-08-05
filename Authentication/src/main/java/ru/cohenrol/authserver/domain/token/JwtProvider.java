package ru.cohenrol.authserver.domain.token;

import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.cohenrol.authserver.datasource.model.ServiceEntity;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.cohenrol.authserver.domain.enums.Role.INTERNAL_SERVICE;
import static ru.cohenrol.authserver.domain.enums.Role.USER;

@Component
@RefreshScope
public class JwtProvider {
    private final PrivateKey privateKey;
    @Getter
    private final PublicKey publicKey;
    //    private final KafkaLogProducerService kafkaLogProducerService;

    @Value("${app.jwt.users.expiration-ms}")
    private long usersJwtExpirationMs;
    @Value("${app.jwt.services.common.expiration-ms}")
    private long servicesJwtExpirationMs;

    @Value("${app.jwt.common.key-id}")
    private String jwtKeyId;
    @Value("${app.jwt.common.issuer}")
    private String issuer;

    @Value("${app.jwt.users.access-key-audience}")
    List<String> usersAccessKeyAudience;
    @Value("${app.jwt.services.wish.access-key-audience}")
    List<String> servicesAccessKeyAudience;

    public JwtProvider(
            @Value("${app.jwt.common.keystore.location}") Resource storeLocation,
            @Value("${app.jwt.common.keystore.password}") String storePassword,
            @Value("${app.jwt.common.keystore.alias}") String keyAlias
//            ,KafkaLogProducerService kafkaLogProducerService
    ) {
//        this.kafkaLogProducerService = kafkaLogProducerService;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream inputStream = storeLocation.getInputStream()) {
                keyStore.load(inputStream, storePassword.toCharArray());
            }
            this.privateKey = (PrivateKey) keyStore.getKey(keyAlias, storePassword.toCharArray());
            this.publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки KeyStore", e);
        }
    }

    public String generateAccessToken(UUID userUuid) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + usersJwtExpirationMs);

        return Jwts.builder()
            .header()
            .keyId(jwtKeyId)
            .and()
            .issuer(issuer)
            .audience().add(usersAccessKeyAudience).and()
            .subject(userUuid.toString())
            .claims(Map.of("roles", List.of(USER.name())))
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(privateKey, Jwts.SIG.ES256)
            .compact();
    }

    public String generateToken(ServiceEntity service) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + servicesJwtExpirationMs);

        return Jwts.builder()
            .header()
            .keyId(jwtKeyId)
            .and()
            .issuer(issuer)
            .audience().add(servicesAccessKeyAudience).and()
            .subject(service.getServiceName())
            .claims(Map.of("roles", List.of(INTERNAL_SERVICE.name())))
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(privateKey, Jwts.SIG.ES256)
            .compact();
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

//    public String getPublicKeyAsBase64() {
//        return Base64.getEncoder().encodeToString(this.publicKey.getEncoded());
//    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.publicKey) // Используем уже готовый открытый ключ
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
