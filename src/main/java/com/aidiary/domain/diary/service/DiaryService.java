package com.aidiary.domain.diary.service;

import com.aidiary.domain.diary.dto.CreateDiaryServiceRequestDto;
import com.aidiary.domain.diary.dto.UpdateDiaryServiceRequestDto;
import com.aidiary.domain.diary.entity.Diary;
import com.aidiary.domain.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@RequiredArgsConstructor
@Transactional
@Service
public class DiaryService {

	private final DiaryRepository diaryRepository;

	public List<Diary> getAllDiaries() {
		return diaryRepository.findAll();
	}

	public List<Diary> getDiariesByDateAndUserId(LocalDate date, String userId) {
		return diaryRepository.findByDateAndUserId(date, userId);
	}

	public Diary getDiaryById(Long id) {
		return diaryRepository.findById(id).orElse(null);
	}
	public List<Diary> getDiariesByUserId(String userId) {
		return diaryRepository.findByUserId(userId);
	}

	private String getEmotion() {
		return "happy";
	}

	private String getUserId() {
		return "uid0000000000";
	}

	public Diary saveDiary(CreateDiaryServiceRequestDto dto) {
		return diaryRepository.save(dto.toEntity(LocalDate.now(), getEmotion(), true, getUserId()));
	}

	public Diary updateDiary(UpdateDiaryServiceRequestDto dto) {
		Optional<Diary> optionalDiary = diaryRepository.findById(dto.id());
		if(optionalDiary.isEmpty()) {
			// error
		}
		Diary diary = optionalDiary.get();
		diary.updateDiary(dto);
		return diaryRepository.save(diary);
	}

	public void deleteDiary(Long id) {
		Optional<Diary> optionalDiary = diaryRepository.findById(id);
		if(optionalDiary.isEmpty()) {
			// return error
		}
		diaryRepository.deleteById(id);
		// return message
	}

	public List<Diary> searchDiariesByKeywordAndUserId(String keyword, String userId) {
		return diaryRepository.findByContentContainingAndUserId(keyword, userId);
	}

//	public Diary autoSaveDiary(Diary diary) {
//		Optional<Diary> optionalDiary = diaryRepository.findById(id);
//		return diaryRepository.save(diary);
//	}
}


