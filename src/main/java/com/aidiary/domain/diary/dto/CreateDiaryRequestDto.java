package com.aidiary.domain.diary.dto;

public record CreateDiaryRequestDto (
        String content
) {
    public CreateDiaryServiceRequestDto toServiceRequest() {
        return new CreateDiaryServiceRequestDto(content);
    }
}
