package com.aidiary.domain.chatbot.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record QueryReq(
        String userId,
        String question,
        List<String> chatHistory
) {
}
