package com.aidiary.domain.diary.domain.repository;

import com.aidiary.domain.diary.domain.QDiary;
import com.aidiary.domain.home.dto.HomeViewRes;
import com.aidiary.domain.home.dto.QHomeViewRes;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
}
