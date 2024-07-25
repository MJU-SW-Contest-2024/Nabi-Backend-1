package com.aidiary.domain.diary.dto;

import com.aidiary.domain.diary.entity.Diary;

import java.time.LocalDate;

public record CreateDiaryServiceRequestDto(
        String content
) {
    public Diary toEntity(LocalDate date, String emotion, boolean status, String userId) {
        return Diary.builder()
                .date(date)
                .content(content)
                .emotion(emotion)
                .status(status)
                .userId(userId)
                .build();
    }
}
