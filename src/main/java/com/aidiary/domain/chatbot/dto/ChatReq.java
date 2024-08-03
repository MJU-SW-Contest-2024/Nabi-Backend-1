package com.aidiary.domain.chatbot.dto;

import lombok.Builder;

@Builder
public record ChatReq(
        String question
) {
}
