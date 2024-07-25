package com.aidiary.domain.diary.presentation;

import com.aidiary.domain.diary.application.DiaryService;
import com.aidiary.domain.diary.dto.CreateDiaryReq;
import com.aidiary.domain.diary.dto.CreateDiaryRes;
import com.aidiary.global.config.security.token.CurrentUser;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.aidiary.global.payload.ErrorResponse;
import com.aidiary.global.payload.ResponseCustom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Diary", description = "Diary API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/diarys")
public class DiaryController {

    private final DiaryService diaryService;


    @Operation(summary = "일기 생성", description = "일기를 작성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 생성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CreateDiaryRes.class))}),
            @ApiResponse(responseCode = "400", description = "일기 생성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping
    public ResponseCustom<CreateDiaryRes> createDiary(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 CreateDiaryReq를 참고해주세요.", required = true) @Valid @RequestBody CreateDiaryReq createDiaryReq) {
        return ResponseCustom.OK(diaryService.writeDiary(userPrincipal, createDiaryReq));
    }

}
