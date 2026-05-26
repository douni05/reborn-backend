package com.jimmy.reborn_backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FastApiResponseDto {

    private String label;          // 인식된 물체명 (예: Jeans)
    private String materialType;   // 재질 한국어 (예: 데님)
    private String conditionGrade; // 상태 등급 (A/B/C)
    private Boolean isReformable;  // 리폼 가능 여부
    private String difficulty;     // 난이도 (Easy/Normal/Hard)
    private String reformTitle;    // 리폼 제목 (예: 청바지로 토트백 만들기)
    private String reformPlan;     // 단계별 가이드 (\n으로 구분)
    private String materials;      // 필요한 재료
    private String estimatedTime;  // 예상 소요 시간
    private String estimatedCost;  // 예상 비용

    // FastAPI 없을 때 fallback용 생성자
    public FastApiResponseDto(String label, String materialType,
                              String conditionGrade, Boolean isReformable,
                              String reformPlan, String difficulty) {
        this.label = label;
        this.materialType = materialType;
        this.conditionGrade = conditionGrade;
        this.isReformable = isReformable;
        this.reformPlan = reformPlan;
        this.difficulty = difficulty;
    }
}