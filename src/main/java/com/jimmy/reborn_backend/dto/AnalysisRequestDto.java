package com.jimmy.reborn_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequestDto {
    private String label;
    private String imageBase64;
}