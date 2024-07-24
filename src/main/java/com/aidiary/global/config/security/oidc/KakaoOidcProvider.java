package com.aidiary.global.config.security.oidc;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Component
public class KakaoOidcProvider implements OidcProvider{

    private final JwkProvider jwkProvider;

    public KakaoOidcProvider(@Value("${oauth.kakao.public-key-info}") String jwkUrl) {
        try {
            jwkProvider = new JwkProviderBuilder(new URL(jwkUrl)).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JwkProvider", e);
        }
    }



    @Override
    public String getProviderId(String idToken) {
        try {
            DecodedJWT jwt = JWT.decode(idToken);
            Jwk jwk = jwkProvider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);
            return jwt.getClaim("id").asString();
        } catch (Exception e) {
            throw new RuntimeException("Invalid ID token", e);
        }
    }

}
