package com.aidiary.domain.summary.domain.repository;

import com.aidiary.domain.summary.domain.DiarySummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiarySummaryRepository extends JpaRepository<DiarySummary, Long> {
}
