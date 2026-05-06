package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DisposalGuideResponseDto {
    private Long guideId;
    private String materialType;
    private String categoryIcon;
    private String dischargeMethod;
}
