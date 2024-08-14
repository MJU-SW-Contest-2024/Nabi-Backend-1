package com.aidiary.domain.notification.presentation;

import com.aidiary.domain.auth.dto.AuthRes;
import com.aidiary.domain.notification.application.FcmService;
import com.aidiary.domain.notification.dto.FcmReq;
import com.aidiary.domain.notification.dto.FcmTokenReq;
import com.aidiary.domain.notification.dto.NotificationRes;
import com.aidiary.global.config.security.token.CurrentUser;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.aidiary.global.payload.ErrorResponse;
import com.aidiary.global.payload.Message;
import com.aidiary.global.payload.ResponseCustom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Notification",description = "Notification API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class NotificationController {

    private final FcmService fcmService;


    @Operation(summary = "Fcmtoken 저장", description = "해당 유저의 Fcmtoken을 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fcmtoken 저장 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Message.class)))}),
            @ApiResponse(responseCode = "400", description = "Fcmtoken 저장 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/register")
    public ResponseCustom<Message> registerFcmToken(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Fcmtoken을 입력해주세요.", required = true) @RequestBody FcmTokenReq fcmTokenReq) {
        return ResponseCustom.OK(fcmService.register(userPrincipal, fcmTokenReq));
    }

    @Operation(summary = "모든 유저에게 알림 보내기", description = "모든 유저에게 알림을 보냅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 전송 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(responseCode = "400", description = "알림 전송 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/broadcast")
    public ResponseCustom<?> broadcastMessage(@RequestBody FcmReq fcmReq) throws IOException {

        fcmService.broadcastMessage(fcmReq.title(), fcmReq.body());

        return ResponseCustom.OK("Message is pushed");
    }

    @Operation(summary = "알림 내역 조회하기", description = "유저가 받은 모든 알림을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NotificationRes.class)))}),
            @ApiResponse(responseCode = "400", description = "알림 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/notification")
    public ResponseCustom<List<NotificationRes>> getNotifications() {
        return ResponseCustom.OK(fcmService.getNoti());
    }

}
