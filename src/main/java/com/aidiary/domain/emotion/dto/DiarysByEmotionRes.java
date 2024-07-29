package com.aidiary.domain.emotion.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiarysByEmotionRes(
        String content,
        LocalDate diaryEntryDate
) {

    @QueryProjection
    public DiarysByEmotionRes(String content, LocalDate diaryEntryDate) {
        this.content = content;
        this.diaryEntryDate = diaryEntryDate;
    }
}
