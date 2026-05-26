package com.jimmy.reborn_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequestDto {
    private String label; // FastAPI의 LabelRequest와 이름을 똑같이 'label'로 맞춰야 합니다.
}