package com.aidiary.domain.chatbot.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DiaryEmbeddingReq(
        String userId,
        String summarizedDiary
) {
}
