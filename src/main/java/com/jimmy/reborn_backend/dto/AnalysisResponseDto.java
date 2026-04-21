package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResponseDto {
    private Long analysisId;
    private String materialType;   // AI 분석 재질 (예: 청바지) [cite: 166]
    private String conditionGrade; // 상태 등급 (예: A, B)
    private Boolean isReformable;  // 리폼 가능 여부
}