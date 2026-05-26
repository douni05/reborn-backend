package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.ReformPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReformPlanRepository extends JpaRepository<ReformPlan, Long> {
    java.util.Optional<ReformPlan> findByAnalysis_AnalysisId(Long analysisId);
}