package com.aidiary.domain.diary.controller;


import com.aidiary.domain.diary.dto.CreateDiaryRequestDto;
import com.aidiary.domain.diary.dto.UpdateDiaryRequestDto;
import com.aidiary.domain.diary.entity.Diary;
import com.aidiary.domain.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping
    public List<Diary> getAllDiaries() {
        return diaryService.getAllDiaries();
    }

    @GetMapping("/{id}")
    public Diary getDiaryById(@PathVariable Long id) {
        return diaryService.getDiaryById(id);
    }

    @GetMapping("/date")
    public List<Diary> getDiariesByDateAndUserId(@RequestParam String date, @RequestParam String userId) {
        LocalDate localDate = LocalDate.parse(date);
        return diaryService.getDiariesByDateAndUserId(localDate, userId);
    }

    @PostMapping
    public Diary createDiary(@RequestBody CreateDiaryRequestDto dto) {
        return diaryService.saveDiary(dto.toServiceRequest());
    }

    @PutMapping("/{id}")
    public Diary updateDiary(@PathVariable Long id, @RequestBody UpdateDiaryRequestDto dto) {
        return diaryService.updateDiary(dto.toServiceRequest(id));
    }
    @DeleteMapping("/{id}")
    public void deleteDiary(@PathVariable Long id) {
        // return
        diaryService.deleteDiary(id);
    }

    @GetMapping("/search")
    public List<Diary> searchDiaries(@RequestParam String keyword, @RequestParam String userId) {
        return diaryService.searchDiariesByKeywordAndUserId(keyword, userId);
    }
    @GetMapping("/get")
    public List<Diary> getDiariesbyUserId(@RequestParam String userId) {
        return diaryService.getDiariesByUserId(userId);
    }

//    @PostMapping("/autosave")
//    public Diary autoSaveDiary(@RequestBody UpdateDiaryRequestDto dto) {
//        return diaryService.autoSaveDiary(dto.toServiceRequest(dto));
//    }
}

