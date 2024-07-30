package com.aidiary.domain.diary.dto;

import com.querydsl.core.annotations.QueryProjection;
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
    @QueryProjection
    public DiaryDetailsRes(Long diaryId, String nickname, String content, LocalDate diaryEntryDate, String emotion) {
        this.diaryId = diaryId;
        this.nickname = nickname;
        this.content = content;
        this.diaryEntryDate = diaryEntryDate;
        this.emotion = emotion;
    }
}
