package com.aidiary.domain.chatbot.domain.repository;

import com.aidiary.domain.chatbot.domain.ChatHistory;
import com.aidiary.domain.chatbot.domain.QChatHistory;
import com.aidiary.domain.chatbot.dto.ChatHistoryRes;
import com.aidiary.domain.chatbot.dto.QChatHistoryRes;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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
                .limit(60)
                .fetch();
    }

    @Override
    public Page<ChatHistoryRes> findRecent20ChatHistories(Long id, Pageable pageable) {

        JPAQuery<Long> countQuery;

        List<ChatHistoryRes> results = queryFactory
                .select(new QChatHistoryRes(
                        chatHistory.id,
                        chatHistory.message,
                        chatHistory.chatRole,
                        chatHistory.createdAt
                ))
                .from(chatHistory)
                .where(chatHistory.user.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(chatHistory.createdAt.desc())
                .fetch();

        countQuery = queryFactory
                .select(chatHistory.count())
                .from(chatHistory)
                .where(chatHistory.user.id.eq(id));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public ChatHistory findMostRecentChatHistoryByUserId(Long id) {
        return queryFactory
                .select(chatHistory)
                .from(chatHistory)
                .where(chatHistory.user.id.eq(id))
                .orderBy(chatHistory.id.desc())
                .limit(1)
                .fetchOne();
    }
}
