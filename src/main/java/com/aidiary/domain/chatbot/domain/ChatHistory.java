package com.aidiary.domain.chatbot.domain;

import com.aidiary.domain.common.BaseEntity;
import com.aidiary.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private ChatRole chatRole;


    @Builder
    public ChatHistory(User user, String message, ChatRole chatRole) {
        this.user = user;
        this.message = message;
        this.chatRole = chatRole;
    }
}
