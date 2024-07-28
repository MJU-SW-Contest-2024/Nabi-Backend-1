package com.aidiary.domain.diary.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SearchDiariesRes(
        String previewContent,
        LocalDate diaryEntryDate,
        String surroundingText
) {

    @QueryProjection
    public SearchDiariesRes(String previewContent, LocalDate diaryEntryDate, String surroundingText) {
        this.previewContent = previewContent;
        this.diaryEntryDate = diaryEntryDate;
        this.surroundingText = surroundingText;
    }
}
