package com.aidiary.domain.auth.application;

import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
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


    public String findEmail(String providerId) {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(EntityNotFoundException::new);
        return user.getEmail();
    }
}
