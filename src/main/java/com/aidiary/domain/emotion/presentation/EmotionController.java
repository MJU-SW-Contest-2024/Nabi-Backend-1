package com.aidiary.domain.emotion.presentation;

import com.aidiary.domain.diary.domain.repository.DiaryRepository;
import com.aidiary.domain.emotion.application.EmotionService;
import com.aidiary.domain.emotion.dto.ChatGPTReq;
import com.aidiary.domain.emotion.dto.ChatGPTRes;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Tag(name = "Emotion", description = "Emotion API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/emotion")
public class EmotionController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate restTemplate;

    private final EmotionService emotionService;
    private final DiaryRepository diaryRepository;


    @Operation(summary = "일기 감정 분석", description = "작성된 일기 내용을 바탕으로 '행복', '화남', '우울', '불안'으로 지배적인 감정을 분석합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 감정 분석 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "일기 감정 분석 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/analyze/{diaryId}")
    public ResponseCustom<String> analyzeEmotion(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "판정할 일기의 id를 입력해주세요.", required = true) @PathVariable Long diaryId
    ) {
        String prompt = emotionService.loadDiaryContent(userPrincipal, diaryId);
        String concatedPrompt = prompt.concat("\n위 일기 내용에 대한 지배적인 감정 한가지를 '우울', '불안', '화남', '행복' 중 1개 골라서 판정해줘.");
        ChatGPTReq request = new ChatGPTReq(model, concatedPrompt);
        ChatGPTRes chatGPTRes = restTemplate.postForObject(apiURL, request, ChatGPTRes.class);
        return ResponseCustom.OK(chatGPTRes.getChoices().get(0).getMessage().getContent());
    }
}
