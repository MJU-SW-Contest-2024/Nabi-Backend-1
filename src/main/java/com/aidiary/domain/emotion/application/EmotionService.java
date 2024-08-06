package com.aidiary.domain.emotion.application;

import com.aidiary.domain.diary.domain.Diary;
import com.aidiary.domain.diary.domain.repository.DiaryRepository;
import com.aidiary.domain.emotion.dto.DiarysByEmotionRes;
import com.aidiary.domain.emotion.dto.EmotionStatRes;
import com.aidiary.domain.user.domain.User;
import com.aidiary.domain.user.domain.repository.UserRepository;
import com.aidiary.global.config.security.token.UserPrincipal;
import com.aidiary.global.payload.Message;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmotionService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    @Transactional
    public String loadDiaryContent(UserPrincipal userPrincipal, Long diaryId) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(EntityNotFoundException::new);

        if (diary.getUser().equals(user)) {
            return diary.getContent();
        }

        return "";

    }

    @Transactional
    public Message saveEmotion(UserPrincipal userPrincipal, Long diaryId, String emotionState) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(EntityNotFoundException::new);

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(EntityNotFoundException::new);

        if (diary.getUser().equals(user)) {
            diary.updateEmotion(emotionState);

            return Message.builder()
                    .message("감정 저장에 성공했습니다.")
                    .build();
        }


        return Message.builder()
                .message("감정 저장에 실패했습니다.")
                .build();
    }

    public EmotionStatRes loadEmotionStat(UserPrincipal userPrincipal, LocalDate startDate, LocalDate endDate) {

        EmotionStatRes emotionsCountBetweenStartDateAndEndDate = diaryRepository.findEmotionsCountBetweenStartDateAndEndDate(userPrincipal.getId(), startDate, endDate);

        return emotionsCountBetweenStartDateAndEndDate;
    }

    @Transactional
    public Slice<DiarysByEmotionRes> findDiarys(UserPrincipal userPrincipal, String emotion, Pageable pageable) {
        Slice<DiarysByEmotionRes> allByEmotionAndUserId = diaryRepository.findAllByEmotionAndUserId(emotion, userPrincipal.getId(), pageable);
        return allByEmotionAndUserId;
    }
}
