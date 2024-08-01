package com.aidiary.domain.notification.presentation;

import com.aidiary.domain.notification.application.FcmService;
import com.aidiary.domain.notification.dto.FcmReq;
import com.aidiary.domain.notification.dto.FcmTokenReq;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    @Operation(summary = "Test용 알람", description = "이 API는 Test용으로 서버측 에서만 현재 컨트롤 할 수 있습니다.")
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
