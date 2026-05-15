package com.jimmy.reborn_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReformRequestCreateDto {
    private Long shopId;
    private String designTitle;
    private String requestContent;
}
