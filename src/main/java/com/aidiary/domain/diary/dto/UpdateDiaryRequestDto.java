package com.aidiary.domain.diary.dto;

import java.time.LocalDate;

public record UpdateDiaryRequestDto(
        String content
) {
    public UpdateDiaryServiceRequestDto toServiceRequest(Long id) {
        return new UpdateDiaryServiceRequestDto(id, content);
    }
}
