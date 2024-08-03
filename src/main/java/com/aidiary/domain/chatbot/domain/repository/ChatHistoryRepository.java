package com.aidiary.domain.chatbot.domain.repository;

import com.aidiary.domain.chatbot.domain.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long>, ChatHistoryQueryDslRepository {
}
