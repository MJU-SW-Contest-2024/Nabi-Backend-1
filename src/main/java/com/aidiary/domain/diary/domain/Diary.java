package com.aidiary.domain.diary.domain;

import com.aidiary.domain.common.BaseEntity;
import com.aidiary.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "diary_entry_date")
    private LocalDate diaryEntryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;


    @Builder
    public Diary(User user, String content, LocalDate diaryEntryDate) {
        this.user = user;
        this.content = content;
        this.diaryEntryDate = diaryEntryDate;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDiaryEntryDate(LocalDate diaryEntryDate) {
        this.diaryEntryDate = diaryEntryDate;
    }

    public void updateEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
}