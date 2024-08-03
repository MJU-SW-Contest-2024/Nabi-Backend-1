package com.aidiary.domain.chatbot.application;

import com.aidiary.domain.chatbot.domain.ChatHistory;
import com.aidiary.domain.chatbot.domain.ChatRole;
import com.aidiary.domain.chatbot.domain.repository.ChatHistoryRepository;
import com.aidiary.domain.chatbot.dto.DiaryEmbeddingReq;
import com.aidiary.domain.chatbot.dto.QueryReq;
import com.aidiary.domain.summary.domain.DiarySummary;
import com.aidiary.domain.summary.domain.repository.DiarySummaryRepository;
import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
import com.aidiary.global.config.security.token.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatbotService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final DiarySummaryRepository diarySummaryRepository;
    private final UserRepository userRepository;


    @Transactional
    public DiaryEmbeddingReq makeRequest(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        DiarySummary diarySummary = diarySummaryRepository.findByUser(user)
                .orElseThrow(EntityNotFoundException::new);

        DiaryEmbeddingReq diaryEmbeddingReq = DiaryEmbeddingReq.builder()
                .userId(id.toString())
                .summarizedDiary(diarySummary.getSummarizedDiary())
                .build();

        return diaryEmbeddingReq;
    }

    @Transactional
    public QueryReq makeQuery(UserPrincipal userPrincipal, String question) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);


        // 챗 히스토리를 최근 20개 이하로 가져온다
        List<ChatHistory> recentChatHistoryByUserId = chatHistoryRepository.findRecentChatHistoryByUserId(userPrincipal.getId());
        List<String> chatHistoryList = recentChatHistoryByUserId.stream()
                .map(chat -> String.format("%s: %s", chat.getChatRole() == ChatRole.USER ? "사용자" : "친구", chat.getMessage()))
                .toList();

        log.info("User ID: {}", userPrincipal.getId());
        log.info("Question: {}", question);
        log.info("Chat History: {}", chatHistoryList);

        ChatHistory chatHistory = ChatHistory.builder()
                .user(user)
                .message(question)
                .chatRole(ChatRole.USER)
                .build();


        chatHistoryRepository.save(chatHistory);

        QueryReq queryReq = QueryReq.builder()
                .userId(userPrincipal.getId().toString())
                .question(question)
                .chatHistory(chatHistoryList)
                .build();

        return queryReq;

    }

    @Transactional
    public void registerBotChat(UserPrincipal userPrincipal, String result) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);

        ChatHistory chatHistory = ChatHistory.builder()
                .user(user)
                .message(result)
                .chatRole(ChatRole.BOT)
                .build();
        chatHistoryRepository.save(chatHistory);
    }
}
