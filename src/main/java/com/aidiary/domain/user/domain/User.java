package com.aidiary.domain.user.domain;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String username;

    private String email;

    private String role;


    @Builder
    public User(String nickname, String username, String email, String role) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
