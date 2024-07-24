package com.aidiary.global.config.security.oidc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


import static org.apache.commons.codec.binary.Base64.decodeBase64;

public interface OidcProvider {
    String getProviderId(String idToken);

    default Map<String, String> parseHeaders(String token) {
        String header = token.split("\\.")[0];

        try {
            return new ObjectMapper().readValue(decodeBase64(header), Map.class);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
