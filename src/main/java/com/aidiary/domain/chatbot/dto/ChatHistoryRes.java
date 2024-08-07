package com.aidiary.domain.chatbot.dto;

import com.aidiary.domain.chatbot.domain.ChatRole;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record ChatHistoryRes(
        Long chatId,
        String message,
        ChatRole chatRole
) {

    @QueryProjection
    public ChatHistoryRes(Long chatId, String message, ChatRole chatRole) {
        this.chatId = chatId;
        this.message = message;
        this.chatRole = chatRole;
    }
}
