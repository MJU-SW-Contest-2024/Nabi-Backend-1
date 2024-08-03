package com.aidiary.domain.chatbot.domain.repository;

import com.aidiary.domain.chatbot.domain.ChatHistory;

import java.util.List;

public interface ChatHistoryQueryDslRepository {
    List<ChatHistory> findRecentChatHistoryByUserId(Long id);
}
