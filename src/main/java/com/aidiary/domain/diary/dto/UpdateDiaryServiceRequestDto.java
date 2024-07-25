package com.aidiary.domain.diary.dto;

import com.aidiary.domain.diary.entity.Diary;

import java.time.LocalDate;

public record UpdateDiaryServiceRequestDto(
        Long id,
        String content
) {
}
