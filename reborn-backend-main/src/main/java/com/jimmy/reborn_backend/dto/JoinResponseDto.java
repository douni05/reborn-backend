package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinResponseDto {
    private String token;
    private Long userId;
    private String nickname;
    private Integer totalXp;
    private Integer currentLevel;
}
