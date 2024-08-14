package com.aidiary.domain.notification.application;

import com.aidiary.domain.notification.domain.Notification;
import com.aidiary.domain.notification.domain.repository.NotificationRepository;
import com.aidiary.domain.notification.dto.FcmMessage;
import com.aidiary.domain.notification.dto.FcmTokenReq;
import com.aidiary.domain.notification.dto.NotificationRes;
import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.aidiary.global.payload.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
            "nabi-ffce7/messages:send";
    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Msg.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    @Transactional
    public synchronized Message register(UserPrincipal userPrincipal, FcmTokenReq fcmTokenReq) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (fcmTokenReq.fcmToken().isEmpty()) {
            return Message
                    .builder()
                    .message("fcmToken이 비어 있습니다.")
                    .build();
        }

        user.updateFcmToken(fcmTokenReq.fcmToken());

        return Message
                .builder()
                .message("fcmToken이 등록되었습니다.")
                .build();
    }

    @Transactional
    public void broadcastMessage(String title, String body) throws IOException {
        List<String> tokens = userRepository.findAll().stream()
                .map(User::getFcmToken)
                .collect(Collectors.toList());

        Notification notification = Notification.builder()
                .title(title)
                .body(body)
                .build();

        notificationRepository.save(notification);

        for (String token : tokens) {
            sendMessageTo(token, title, body);
        }
    }

    public List<NotificationRes> getNoti() {
        List<Notification> allOrderByCreatedAtDesc = notificationRepository.findAllOrderByCreatedAtDesc();
        List<NotificationRes> notificationResList = new ArrayList<>();
        allOrderByCreatedAtDesc.stream()
                .map(notification -> {
                    NotificationRes notificationRes = NotificationRes.builder()
                            .createdAt(notification.getCreatedAt())
                            .title(notification.getTitle())
                            .body(notification.getBody())
                            .build();

                    notificationResList.add(notificationRes);

                    return notification;
                } )
                .collect(Collectors.toList());

        return notificationResList;
    }
}
