package com.aidiary.domain.chatbot.domain.repository;

import com.aidiary.domain.chatbot.domain.ChatHistory;
import com.aidiary.domain.chatbot.dto.ChatHistoryRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatHistoryQueryDslRepository {
    List<ChatHistory> findRecentChatHistoryByUserId(Long id);

    Page<ChatHistoryRes> findRecent20ChatHistories(Long id, Pageable pageable);

    ChatHistory findMostRecentChatHistoryByUserId(Long id);
}
