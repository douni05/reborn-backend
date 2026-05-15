package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private String reviewerNickname;
    private Integer rating;
    private String content;
    private String createdAt;
}
