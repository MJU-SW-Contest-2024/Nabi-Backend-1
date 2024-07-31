package com.aidiary.domain.auth.application;

import com.aidiary.domain.auth.dto.NicknameRes;
import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public User findOrCreateUser(String provider, String idToken) {

        DecodedJWT decodedJWT = JWT.decode(idToken);
        String providerId = decodedJWT.getSubject();  // 사용자 고유 ID (sub)
        String email = decodedJWT.getClaim("email").asString();
        String username = decodedJWT.getClaim("nickname").asString();

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(provider, providerId);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            User newUser = new User("", username, email, "ROLE_USER", provider, providerId);
            return userRepository.save(newUser);
        }
    }


    @Transactional
    public String findEmail(String providerId) {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(EntityNotFoundException::new);
        return user.getEmail();
    }


    @Transactional
    public NicknameRes updateNickname(UserPrincipal userPrincipal, String nickname) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);

        user.updateNickname(nickname);

        user.updateIsRegistered();

        NicknameRes nicknameRes = NicknameRes.builder()
                .userid(user.getId())
                .nickname(nickname)
                .isRegistered(user.isRegistered())
                .build();


        return nicknameRes;
    }

    @Transactional
    public void updateFcmToken(Long id, String fcmToken) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        user.updateFcmToken(fcmToken);
    }
}
