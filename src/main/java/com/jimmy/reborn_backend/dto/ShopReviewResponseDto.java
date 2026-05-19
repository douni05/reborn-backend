package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ShopReviewResponseDto {
    private Double averageRating;
    private Long reviewCount;
    private List<ReviewResponseDto> reviews;
}
