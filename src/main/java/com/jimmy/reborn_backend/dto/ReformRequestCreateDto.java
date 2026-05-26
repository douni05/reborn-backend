package com.jimmy.reborn_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReformRequestCreateDto {
    private Long shopId;
    private String designTitle;
    private String requestContent;

    // 선택한 리폼 솔루션 정보 (optional)
    private Long planId;
    private String reformPlan;
    private String difficulty;
    private String materials;
    private String estimatedTime;
    private String estimatedCost;
}
