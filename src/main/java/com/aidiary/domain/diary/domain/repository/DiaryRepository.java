package com.aidiary.domain.diary.domain.repository;

import com.aidiary.domain.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
