package com.aidiary.domain.diary.application;

import com.aidiary.domain.diary.domain.Diary;
import com.aidiary.domain.diary.domain.repository.DiaryRepository;
import com.aidiary.domain.diary.dto.CreateDiaryReq;
import com.aidiary.domain.diary.dto.CreateDiaryRes;
import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
import com.aidiary.global.config.security.token.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public CreateDiaryRes writeDiary(UserPrincipal userPrincipal, CreateDiaryReq createDiaryReq) {

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(EntityNotFoundException::new);

        Diary diary = Diary.builder()
                .user(user)
                .content(createDiaryReq.content())
                .diaryEntryDate(createDiaryReq.diaryEntryDate())
                .build();

        diaryRepository.save(diary);

        CreateDiaryRes createDiaryRes = CreateDiaryRes.builder()
                .id(diary.getId())
                .content(diary.getContent())
                .diaryEntryDate(diary.getDiaryEntryDate())
                .build();

        return createDiaryRes;
    }
}
