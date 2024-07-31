package com.aidiary.domain.home.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HomeViewRes(
        String content,
        LocalDate diaryEntryDate,
        String emotion,
        Boolean isBookmarked
) {

    @QueryProjection
    public HomeViewRes(String content, LocalDate diaryEntryDate, String emotion, Boolean isBookmarked) {
        this.content = content;
        this.diaryEntryDate = diaryEntryDate;
        this.emotion = emotion;
        this.isBookmarked = isBookmarked;
    }
}
