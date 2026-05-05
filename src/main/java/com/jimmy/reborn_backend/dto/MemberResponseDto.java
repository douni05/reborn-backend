package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {
    private Long userId;
    private String nickname;
    private Integer totalXp;
    private Integer currentLevel;
    private Integer totalReformCount;
    private Integer totalDisposalCount;
}