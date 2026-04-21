package com.jimmy.reborn_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnalysisRequestDto {
    private String originImgUrl; // 사용자가 업로드한 이미지 경로
}