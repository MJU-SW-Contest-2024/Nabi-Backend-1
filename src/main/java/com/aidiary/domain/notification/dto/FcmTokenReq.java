package com.aidiary.domain.notification.dto;

import lombok.Builder;

@Builder
public record FcmTokenReq(
        String fcmToken
) {
}
