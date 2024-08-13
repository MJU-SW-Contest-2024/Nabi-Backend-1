package com.aidiary.domain.summary.presentation;

import com.aidiary.domain.summary.application.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final SummaryService summaryService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void run() {
        summaryService.summarize();
    }
}
