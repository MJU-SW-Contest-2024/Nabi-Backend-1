package com.aidiary.domain.auth.presentation;

import com.aidiary.domain.auth.dto.AuthRes;
import com.aidiary.domain.auth.dto.IdTokenReq;
import com.aidiary.domain.auth.service.AuthService;
import com.aidiary.global.config.security.jwt.JWTUtil;
import com.aidiary.global.config.security.oidc.OidcProviderFactory;
import com.aidiary.global.config.security.oidc.Provider;
import com.aidiary.global.payload.ErrorResponse;
import com.aidiary.global.payload.ResponseCustom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authorization", description = "Authorization API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OidcProviderFactory oidcProviderFactory;
    private final JWTUtil jwtUtil;

    private final AuthService authService;


    @Operation(summary = "소셜 로그인", description = "idToken과 provider(ex. kakao)로 소셜 로그인을 수행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AuthRes.class)))}),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/login")
    public ResponseCustom<AuthRes> login(@RequestBody IdTokenReq idTokenReq) {
        String providerId = oidcProviderFactory.getProviderId(
                Provider.valueOf(idTokenReq.provider().toUpperCase()), idTokenReq.idToken());

        if (providerId == null) {
            return ResponseCustom.INVALID_ID_TOKEN();
        }

        authService.findOrCreateUser(idTokenReq.provider(), providerId);

        String accessToken = jwtUtil.createJwt("access", providerId, "ROLE_USER", 3600000L);

        AuthRes authRes = AuthRes.builder()
                .accessToken(accessToken)
                .build();

        return ResponseCustom.OK(authRes);
    }
}
