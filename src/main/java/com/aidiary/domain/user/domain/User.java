package com.aidiary.domain.user.domain;

import com.aidiary.domain.common.BaseEntity;
import com.aidiary.domain.summary.domain.DiarySummary;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String nickname;

    private String username;

    private String email;

    private String role;

    private String provider;

    private String providerId;

    private boolean isRegistered;

    private String fcmToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private DiarySummary diarySummary;


    @Builder
    public User(String nickname, String username, String email, String role, String provider, String providerId) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.isRegistered = false;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateIsRegistered() {
        this.isRegistered = true;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void updateDiarySummary(DiarySummary diarySummary) {
        this.diarySummary = diarySummary;
    }
}
