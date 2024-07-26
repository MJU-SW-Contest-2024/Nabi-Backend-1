package com.aidiary.domain.diary.domain.repository;

import com.aidiary.domain.home.dto.HomeViewRes;
import com.aidiary.global.config.security.token.UserPrincipal;

import java.util.List;

public interface DiaryQueryDslRepository {

    List<HomeViewRes> findRecentFiveDiaryWithAuthorization(Long userId);

    int findConsecutiveWritingDays(Long userId);

}
