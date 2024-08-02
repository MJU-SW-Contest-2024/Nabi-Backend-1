package com.aidiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AiDiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiDiaryApplication.class, args);
    }

}
