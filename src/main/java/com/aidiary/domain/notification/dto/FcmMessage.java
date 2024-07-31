package com.aidiary.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record FcmMessage(
        boolean validateOnly,
        Msg message
) {
    @Builder
    @AllArgsConstructor
    @Getter
    public static class Msg {
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
