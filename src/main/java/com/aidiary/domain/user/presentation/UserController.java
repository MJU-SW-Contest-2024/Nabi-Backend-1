package com.aidiary.domain.user.presentation;

import com.aidiary.domain.user.application.UserService;
import com.aidiary.domain.userInfo.dto.UserInfoRes;
import com.aidiary.global.payload.ErrorResponse;
import com.aidiary.global.payload.Message;
import com.aidiary.global.payload.ResponseCustom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

//    private final UserService userService;
//
//    @Operation(summary = "회원 탈퇴", description = "해당 유저를 회원 탈퇴합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Message.class)))}),
//            @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
//    })
//    @DeleteMapping
//    public ResponseCustom<Message>


}
