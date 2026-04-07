package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.AnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalysisHistoryRepository extends JpaRepository<AnalysisHistory, Long> {
    // 특정 사용자의 분석 이력 전체 조회 기능 추가
    List<AnalysisHistory> findAllByMember_UserId(Long userId);
}