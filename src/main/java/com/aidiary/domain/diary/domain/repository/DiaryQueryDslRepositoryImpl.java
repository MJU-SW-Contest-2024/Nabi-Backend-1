package com.aidiary.domain.diary.domain.repository;

import com.aidiary.domain.diary.domain.QDiary;
import com.aidiary.domain.diary.dto.QSearchDiariesRes;
import com.aidiary.domain.diary.dto.SearchDiariesRes;
import com.aidiary.domain.diary.dto.condition.DiariesSearchCondition;
import com.aidiary.domain.emotion.dto.EmotionStatRes;
import com.aidiary.domain.emotion.dto.QEmotionStatRes;
import com.aidiary.domain.home.dto.HomeViewRes;
import com.aidiary.domain.home.dto.QHomeViewRes;
import com.aidiary.domain.user.domain.User;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.aidiary.domain.diary.domain.QDiary.*;

@RequiredArgsConstructor
@Repository
public class DiaryQueryDslRepositoryImpl implements DiaryQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HomeViewRes> findRecentFiveDiaryWithAuthorization(Long userId) {
        return queryFactory
                .select(new QHomeViewRes(
                        diary.content,
                        diary.diaryEntryDate,
                        diary.emotion
                ))
                .from(diary)
                .orderBy(diary.diaryEntryDate.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public int findConsecutiveWritingDays(Long userId) {
        List<LocalDate> dates = queryFactory
                .select(diary.diaryEntryDate)
                .from(diary)
                .where(diary.user.id.eq(userId))
                .orderBy(diary.diaryEntryDate.desc())
                .fetch();

        if (dates.isEmpty()) {
            return 0;
        }

        int consecutiveDays = 1;
        LocalDate currentDate = dates.get(0);

        for (int i = 1; i < dates.size(); i++) {
            LocalDate nextDate = dates.get(i);
            if (currentDate.minusDays(1).equals(nextDate)) {
                consecutiveDays++;
                currentDate = nextDate;
            } else {
                break;
            }
        }

        return consecutiveDays;
    }

    @Override
    public EmotionStatRes findEmotionsCountBetweenStartDateAndEndDate(Long userId, LocalDate startDate, LocalDate endDate) {


        NumberExpression<Integer> angerCount = new CaseBuilder()
                .when(diary.emotion.eq("화남")).then(1)
                .otherwise(0)
                .sum();

        NumberExpression<Integer> depressionCount = new CaseBuilder()
                .when(diary.emotion.eq("우울")).then(1)
                .otherwise(0)
                .sum();

        NumberExpression<Integer> anxietyCount = new CaseBuilder()
                .when(diary.emotion.eq("걱정")).then(1)
                .otherwise(0)
                .sum();

        NumberExpression<Integer> happinessCount = new CaseBuilder()
                .when(diary.emotion.eq("행복")).then(1)
                .otherwise(0)
                .sum();

        return queryFactory
                .select(new QEmotionStatRes(
                        angerCount,
                        depressionCount,
                        anxietyCount,
                        happinessCount
                ))
                .from(diary)
                .where(
                        diary.user.id.eq(userId), diary.diaryEntryDate.between(startDate, endDate)
                )
                .fetchOne();
    }

    @Override
    public Page<SearchDiariesRes> findDiaries(User user, DiariesSearchCondition diariesSearchCondition, Pageable pageable) {
        String content = diariesSearchCondition.content();

        List<SearchDiariesRes> results;

        JPAQuery<Long> countQuery;


        results = queryFactory
                .select(new QSearchDiariesRes(
                        getExcerpt(diary.content, content),
                        diary.diaryEntryDate,
                        Expressions.constant("test")
                ))
                .from(diary)
                .where(diary.content.contains(content),
                        equalsUser(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(diary.diaryEntryDate.desc())
                .fetch();

        countQuery = queryFactory
                .select(diary.count())
                .from(diary)
                .where(diary.content.contains(content));


        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    public BooleanExpression equalsUser(User user) {
        return user != null ? diary.user.eq(user) : null;
    }

    private StringTemplate getExcerpt(StringPath content, String keyword) {
        return Expressions.stringTemplate(
                "concat(substring({0}, greatest(1, locate({1}, {0}) - 10), least(locate({1}, {0}) - greatest(1, locate({1}, {0}) - 10), 10)), {1}, substring({0}, locate({1}, {0}) + length({1}), 10))",
                content, Expressions.constant(keyword)
        );
    }


}
