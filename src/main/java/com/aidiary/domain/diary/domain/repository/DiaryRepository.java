package com.aidiary.domain.diary.domain.repository;

import com.aidiary.domain.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryQueryDslRepository {
}
