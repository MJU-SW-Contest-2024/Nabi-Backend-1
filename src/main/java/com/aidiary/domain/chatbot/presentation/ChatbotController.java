package com.aidiary.domain.chatbot.presentation;

import com.aidiary.domain.chatbot.application.ChatbotService;
import com.aidiary.domain.chatbot.dto.ChatHistoryRes;
import com.aidiary.domain.chatbot.dto.ChatReq;
import com.aidiary.domain.chatbot.dto.DiaryEmbeddingReq;
import com.aidiary.domain.chatbot.dto.QueryReq;
import com.aidiary.global.config.security.token.CurrentUser;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.aidiary.global.payload.ErrorResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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


    @Operation(summary = "챗봇 첫 인사", description = "챗봇의 첫 인사를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "첫 인사 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(responseCode = "400", description = "첫 인사 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/greeting")
    public ResponseCustom<?> initialGreeting(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return ResponseCustom.OK(chatbotService.greeting(userPrincipal));
    }


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
//        String fastApiUrl = "http://127.0.0.1:8000/add_diary"; //

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
//        String fastApiUrl = "http://127.0.0.1:8000/query";

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

    @Operation(summary = "챗봇 대화 히스토리 가져오기", description = "챗봇과의 대화 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챗봇 대화 내용 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChatHistoryRes.class)))}),
            @ApiResponse(responseCode = "400", description = "챗봇 대화 내용 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/history")
    public ResponseCustom<Page<ChatHistoryRes>> getAllChatHistories(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "조회 할 페이지와 페이지 크기를 입력해주세요.") Pageable pageable
    ) {
        return ResponseCustom.OK(chatbotService.getAllChats(userPrincipal, pageable));
    }

    @Operation(summary = "챗봇에게 답변 다시 받기", description = "챗봇에게 가장 최근 질문으로 답변을 다시 받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 다시 받기 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))}),
            @ApiResponse(responseCode = "400", description = "답변 다시 받기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @PostMapping("/retryChat")
    public ResponseCustom<?> retryChat(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        String fastApiUrl = queryUrl;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        QueryReq queryReq = chatbotService.retryQuery(userPrincipal);

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
