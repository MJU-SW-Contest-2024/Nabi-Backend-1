package com.aidiary.global.config.security.oidc;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class OidcProviderFactory {

    private final Map<Provider, OidcProvider> authProviderMap;
    private final KakaoOidcProvider kakaoOidcProvider;
//    private final GoogleOidcProvider googleOidcProvider;
//    private final NaverOidcProvider naverOidcProvider;

    public OidcProviderFactory(
            KakaoOidcProvider kakaoOidcProvider
//            GoogleOidcProvider googleOidcProvider,
//            NaverOidcProvider naverOidcProvider
    ) {
        authProviderMap = new EnumMap<>(Provider.class);
        this.kakaoOidcProvider = kakaoOidcProvider;
//        this.googleOidcProvider = googleOidcProvider;
//        this.naverOidcProvider = naverOidcProvider;
        initialize();
    }

    private void initialize() {
        authProviderMap.put(Provider.KAKAO, kakaoOidcProvider);
//        authProviderMap.put(Provider.GOOGLE, googleOidcProvider);
//        authProviderMap.put(Provider.NAVER, naverOidcProvider);
    }

    public String getProviderId(Provider provider, String idToken) {
        return getProvider(provider).getProviderId(idToken);
    }


    private OidcProvider getProvider(Provider provider) {
        OidcProvider oidcProvider = authProviderMap.get(provider);
        if (oidcProvider == null) {
            throw new RuntimeException("Wrong provider");
        }
        return oidcProvider;
    }
}
