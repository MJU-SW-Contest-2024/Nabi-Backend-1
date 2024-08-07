package com.aidiary.domain.chatbot.application;

import com.aidiary.domain.chatbot.domain.ChatHistory;
import com.aidiary.domain.chatbot.domain.ChatRole;
import com.aidiary.domain.chatbot.domain.Greeting;
import com.aidiary.domain.chatbot.domain.repository.ChatHistoryRepository;
import com.aidiary.domain.chatbot.domain.repository.GreetingRepository;
import com.aidiary.domain.chatbot.dto.ChatHistoryRes;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatbotService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final DiarySummaryRepository diarySummaryRepository;
    private final UserRepository userRepository;
    private final GreetingRepository greetingRepository;


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

    public Page<ChatHistoryRes> getAllChats(UserPrincipal userPrincipal, Pageable pageable) {
        return chatHistoryRepository.findRecent20ChatHistories(userPrincipal.getId(), pageable);
    }

    @Transactional
    public QueryReq retryQuery(UserPrincipal userPrincipal) {
        // 이전에 받은 BOT 챗을 삭제하고 다시 HUMAN 챗을 가장 최근 채팅으로 만든다.
        ChatHistory mostRecentChat = chatHistoryRepository.findMostRecentChatHistoryByUserId(userPrincipal.getId());
        chatHistoryRepository.delete(mostRecentChat);

        // 챗 히스토리를 최근 20개 이하로 가져온다
        List<ChatHistory> recentChatHistoryByUserId = chatHistoryRepository.findRecentChatHistoryByUserId(userPrincipal.getId());
        List<String> chatHistoryList = recentChatHistoryByUserId.stream()
                .map(chat -> String.format("%s: %s", chat.getChatRole() == ChatRole.USER ? "사용자" : "친구", chat.getMessage()))
                .toList();


        String question;
        ChatHistory chatHistory = chatHistoryRepository.findMostRecentChatHistoryByUserId(userPrincipal.getId());
        question = chatHistory.getMessage();

        QueryReq queryReq = QueryReq.builder()
                .userId(userPrincipal.getId().toString())
                .question(question)
                .chatHistory(chatHistoryList)
                .build();

        return queryReq;
    }

    public String greeting(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);
        Greeting greeting = greetingRepository.findById(1L)
                .orElseThrow(EntityNotFoundException::new);
        return user.getNickname() + "! " + greeting.getInitialGreeting();
    }
}
