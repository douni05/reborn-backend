package com.jimmy.reborn_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateDto {
    private Long requestId;
    private Long shopId;
    private Integer rating;
    private String content;
}
