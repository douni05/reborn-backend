package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.AnalysisRequestDto;
import com.jimmy.reborn_backend.dto.AnalysisResponseDto;
import com.jimmy.reborn_backend.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    // 안드로이드에서 POST /api/v1/analysis/1 형태로 요청이 옵니다.
    @PostMapping("/{userId}")
    public AnalysisResponseDto createAnalysis(@PathVariable Long userId, @RequestBody AnalysisRequestDto dto) {
        // ML Kit가 찾은 라벨이 dto.getLabel()에 담겨있어야 합니다.
        return analysisService.analyzeClothing(userId, dto);
    }
}