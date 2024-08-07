package com.aidiary.domain.chatbot.dto;

import com.aidiary.domain.chatbot.domain.ChatRole;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatHistoryRes(
        Long chatId,
        String message,
        ChatRole chatRole,
        LocalDateTime createdTime
) {

    @QueryProjection
    public ChatHistoryRes(Long chatId, String message, ChatRole chatRole, LocalDateTime createdTime) {
        this.chatId = chatId;
        this.message = message;
        this.chatRole = chatRole;
        this.createdTime = createdTime;
    }
}
