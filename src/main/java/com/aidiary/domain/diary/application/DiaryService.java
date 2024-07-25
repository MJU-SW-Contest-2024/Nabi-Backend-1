package com.aidiary.domain.diary.application;

import com.aidiary.domain.diary.domain.Diary;
import com.aidiary.domain.diary.domain.repository.DiaryRepository;
import com.aidiary.domain.diary.dto.CreateDiaryReq;
import com.aidiary.domain.diary.dto.CreateDiaryRes;
import com.aidiary.domain.diary.dto.EditDiaryReq;
import com.aidiary.domain.diary.dto.EditDiaryRes;
import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.aidiary.global.payload.Message;
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

    @Transactional
    public EditDiaryRes editDiary(UserPrincipal userPrincipal, EditDiaryReq editDiaryReq, Long id) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);

        Diary diary = diaryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        if (user.getId().equals(diary.getUser().getId())) {
            diary.updateContent(editDiaryReq.content());

            EditDiaryRes editDiaryRes = EditDiaryRes.builder()
                    .userId(user.getId())
                    .DiaryId(diary.getId())
                    .content(editDiaryReq.content())
                    .diaryEntryDate(diary.getDiaryEntryDate())
                    .build();

            return editDiaryRes;
        }

        return null;
    }

    @Transactional
    public Message removeDiary(UserPrincipal userPrincipal, Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (diary.getUser().equals(user)) {
            diaryRepository.delete(diary);

            return Message.builder()
                    .message("일기를 삭제하였습니다.")
                    .build();
        }

        return Message.builder()
                .message("일기 삭제에 실패했습니다.")
                .build();
    }
}
