package com.aidiary.domain.notification.presentation;

import com.aidiary.domain.notification.application.FcmService;
import com.aidiary.domain.notification.dto.FcmReq;
import com.aidiary.global.payload.ResponseCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class NotificationController {

    private final FcmService fcmService;


    @PostMapping
    public ResponseCustom<?> pushMessage(@RequestBody FcmReq fcmReq) throws IOException {
        System.out.println(fcmReq.targetToken() + " "
                + fcmReq.title() + " " + fcmReq.body());

        fcmService.sendMessageTo(
                fcmReq.targetToken(),
                fcmReq.title(),
                fcmReq.body()
        );

        return ResponseCustom.OK("Message is pushed");
    }
}
