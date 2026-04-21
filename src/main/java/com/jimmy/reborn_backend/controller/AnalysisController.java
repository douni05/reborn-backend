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

    @PostMapping("/{userId}")
    public AnalysisResponseDto createAnalysis(@PathVariable Long userId, @RequestBody AnalysisRequestDto dto) {
        return analysisService.analyzeClothing(userId, dto);
    }
}