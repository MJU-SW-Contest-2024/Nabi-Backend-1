package com.aidiary.domain.diary.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiaryDetailsRes(
        Long diaryId,
        String nickname,
        String content,
        LocalDate diaryEntryDate,
        String emotion
) {
}
