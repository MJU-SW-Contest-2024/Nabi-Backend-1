package com.aidiary.domain.notification.dto;

public record FcmReq(
        String targetToken,
        String title,
        String body
) {
}
