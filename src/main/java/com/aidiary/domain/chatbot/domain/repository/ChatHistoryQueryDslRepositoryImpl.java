package com.aidiary.domain.chatbot.domain.repository;

import com.aidiary.domain.chatbot.domain.ChatHistory;
import com.aidiary.domain.chatbot.domain.QChatHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.aidiary.domain.chatbot.domain.QChatHistory.chatHistory;

@RequiredArgsConstructor
@Repository
public class ChatHistoryQueryDslRepositoryImpl implements ChatHistoryQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatHistory> findRecentChatHistoryByUserId(Long id) {
        return queryFactory
                .select(chatHistory)
                .from(chatHistory)
                .where(chatHistory.user.id.eq(id))
                .orderBy(chatHistory.createdAt.desc())
                .limit(20)
                .fetch();
    }
}
