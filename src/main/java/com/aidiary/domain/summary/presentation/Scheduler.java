package com.aidiary.domain.summary.presentation;

import com.aidiary.domain.summary.application.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SummaryService summaryService;

    @Scheduled(cron = "0 0 3 ? * MON")
    public void run() {
        summaryService.summarize();
    }
}
