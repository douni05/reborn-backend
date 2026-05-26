package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SolutionResponseDto {
    // 리폼 정보
    private String reformTitle;
    private String difficulty;     // 난이도 (Easy, Normal, Hard) [cite: 201]
    private String guideJson;      // 리폼 방법 [cite: 201]

    // 배출 정보 (리폼 불가 시)
    private String materialType;
    private String dischargeMethod; // 올바른 배출 방법
}