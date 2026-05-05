package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResponseDto {

    private Long analysisId;
    private String materialType;
    private String conditionGrade;
    private Boolean isReformable;

    // 리폼 가능할 때
    private String reformTitle;
    private String reformPlan;
    private String difficulty;
    private String materials;
    private String estimatedTime;
    private String estimatedCost;

    // 리폼 불가능할 때 — 분리배출 정보
    private String disposalIcon;
    private String disposalMethod;
}