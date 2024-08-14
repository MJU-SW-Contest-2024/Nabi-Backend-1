package com.aidiary.domain.notification.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationRes(
        LocalDateTime createdAt,
        String title,
        String body
) {
}
