package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReformRequestDto {
    private Long requestId;
    private String requesterNickname;
    private Long shopId;
    private String shopName;
    private String shopImageUrl;
    private String designTitle;
    private String requestContent;
    private String status;
    private String expertMessage;
    private String createdAt;

    // 리폼 솔루션 정보
    private Long planId;
    private String reformPlan;
    private String difficulty;
    private String materials;
    private String estimatedTime;
    private String estimatedCost;
}
