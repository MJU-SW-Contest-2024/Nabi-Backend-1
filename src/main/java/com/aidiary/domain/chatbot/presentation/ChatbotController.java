package com.aidiary.domain.chatbot.presentation;

import com.aidiary.domain.chatbot.application.ChatbotService;
import com.aidiary.domain.chatbot.dto.ChatReq;
import com.aidiary.domain.chatbot.dto.DiaryEmbeddingReq;
import com.aidiary.domain.chatbot.dto.QueryReq;
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
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Tag(name = "Chatbot", description = "Chatbot API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatbot")
@Slf4j
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${chatbot.fastApi.addDiaryUrl}")
    private String addDiaryUrl;

    @Value("${chatbot.fastApi.queryUrl}")
    private String queryUrl;


    @Operation(summary = "일기 임베딩", description = "유저의 일기를 AI 서버에 임베딩합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 임베딩 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(responseCode = "400", description = "일기 임베딩 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/embedding")
    public ResponseCustom<?> addEmbedding(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        String fastApiUrl = addDiaryUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        DiaryEmbeddingReq madeRequest = chatbotService.makeRequest(userPrincipal.getId());

        HttpEntity<DiaryEmbeddingReq> requestEntity = new HttpEntity<>(madeRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, requestEntity, String.class);

        JSONObject responseBody = new JSONObject(response.getBody());
        String message = responseBody.getString("message");

        return ResponseCustom.OK(message);
    }

    @Operation(summary = "챗봇과 대화하기", description = "임베딩된 일기를 바탕으로 챗봇과 대화합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챗봇 대화 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(responseCode = "400", description = "챗봇 대화 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/chat")
    public ResponseCustom<?> chat(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ChatReq chatReq
    ) {
        String fastApiUrl = queryUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        QueryReq queryReq = chatbotService.makeQuery(userPrincipal, chatReq.question());

        log.info("Requesting FastAPI with QueryReq: {}", queryReq);

        HttpEntity<QueryReq> requestEntity = new HttpEntity<>(queryReq, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, requestEntity, String.class);

        // JSON 응답에서 message 필드만 추출
        JSONObject responseBody = new JSONObject(response.getBody());
        String message = responseBody.getString("message");

        // Bot 응답 저장
        chatbotService.registerBotChat(userPrincipal, message);

        return ResponseCustom.OK(message);
    }
}
