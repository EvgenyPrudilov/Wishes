package ru.cohenrol.authserver.domain.service;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import ru.cohenrol.authserver.domain.model.JwkKey;
import ru.cohenrol.authserver.domain.model.JwtSet;
import ru.cohenrol.authserver.domain.token.JwtProvider;

import java.security.interfaces.ECPublicKey;
import java.util.List;

@Service
@RequiredArgsConstructor
@RefreshScope
public class JwtSetService {
    private final JwtProvider jwtProvider;
    @Value("${app.jwt.common.key-id}")
    private String jwtKeyId;

    public JwtSet getJwkSet() {
        ECKey jwk = new ECKey.Builder(Curve.P_256, (ECPublicKey) jwtProvider.getPublicKey())
            .keyID(jwtKeyId)
            .build();

        JwkKey key = JwkKey.builder()
            .kty(jwk.getKeyType().getValue())
            .crv(jwk.getCurve().getName())
            .x(jwk.getX().toString())
            .y(jwk.getY().toString())
            .kid(jwk.getKeyID())
            .build();

        return new JwtSet(List.of(key));
    }
}
