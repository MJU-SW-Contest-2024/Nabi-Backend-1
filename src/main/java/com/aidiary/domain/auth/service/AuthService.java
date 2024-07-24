package com.aidiary.domain.auth.service;

import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
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
    public User findOrCreateUser(String provider, String providerId) {

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(provider, providerId);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            User newUser = new User("", optionalUser.get().getUsername(), optionalUser.get().getEmail(), "ROLE_USER", provider, providerId);
            return userRepository.save(newUser);
        }
    }

}
