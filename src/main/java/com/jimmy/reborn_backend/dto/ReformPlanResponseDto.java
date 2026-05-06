package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReformPlanResponseDto {
    private Long planId;
    private Long analysisId;
    private String reformTitle;
    private String difficulty;
    private String materials;
    private String estimatedTime;
    private String estimatedCost;
    private String guideJson;
    private String resultImgUrl;
}
