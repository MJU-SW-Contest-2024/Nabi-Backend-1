package com.aidiary.domain.chatbot.domain.repository;

import com.aidiary.domain.chatbot.domain.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {
}
