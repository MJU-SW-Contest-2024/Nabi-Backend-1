package com.aidiary.domain.diary.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import com.aidiary.domain.diary.entity.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
	List<Diary> findByDateAndUserId(LocalDate date, String userId);
	List<Diary> findByContentContainingAndUserId(String keyword, String userId);
	List<Diary> findByUserId(String userId);
}
