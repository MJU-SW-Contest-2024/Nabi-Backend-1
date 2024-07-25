package com.aidiary.domain.user.domain;

import com.aidiary.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String username;

    private String email;

    private String role;

    private String provider;

    private String providerId;


    @Builder
    public User(String nickname, String username, String email, String role, String provider, String providerId) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
