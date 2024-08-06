package com.aidiary.domain.emotion.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

@Builder
public record EmotionStatRes(
        Integer angerCount,
        Integer depressionCount,
        Integer anxietyCount,
        Integer HappinessCount,
        Integer BoringCount
) {

    @QueryProjection
    public EmotionStatRes(Integer angerCount, Integer depressionCount, Integer anxietyCount, Integer HappinessCount, Integer BoringCount) {
        this.angerCount = angerCount;
        this.depressionCount = depressionCount;
        this.anxietyCount = anxietyCount;
        this.HappinessCount = HappinessCount;
        this.BoringCount = BoringCount;
    }
}
