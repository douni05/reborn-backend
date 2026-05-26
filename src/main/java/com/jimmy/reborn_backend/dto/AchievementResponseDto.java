package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AchievementResponseDto {
    private Long achievementId;
    private Integer levelThreshold;
    private String titleName;
    private String iconUrl;
}
