package com.aidiary.domain.diary.entity;


import java.time.LocalDate;

import com.aidiary.domain.diary.dto.UpdateDiaryServiceRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Diary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;
	private String content;
	private String emotion;
	private String userId;
	private boolean status;


	@Builder
	public Diary(LocalDate date, String content, String emotion, boolean status, String userId) {
		this.date = date;
		this.content = content;
		this.emotion = emotion;
		this.status = status;
		this.userId = userId;
	}

	public Diary() {

	}

	public void updateDiary(UpdateDiaryServiceRequestDto dto) {
		this.setContent(dto.content());
	}
}

