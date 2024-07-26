package com.aidiary.domain.home.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record HomeViewRes(
        String content,
        LocalDate diaryEntryDate,
        String emotion
) {

    @QueryProjection
    public HomeViewRes(String content, LocalDate diaryEntryDate, String emotion) {
        this.content = content;
        this.diaryEntryDate = diaryEntryDate;
        this.emotion = emotion;
    }
}
