package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.AnalysisRequestDto;
import com.jimmy.reborn_backend.dto.AnalysisResponseDto;
import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public AnalysisResponseDto createAnalysis(
            @RequestHeader("Authorization") String authorization,
            @RequestBody AnalysisRequestDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return analysisService.analyzeClothing(userId, dto);
    }

    @GetMapping("/history")
    public List<AnalysisResponseDto> getHistory(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return analysisService.getHistory(userId);
    }

    @GetMapping("/{id}")
    public AnalysisResponseDto getAnalysisDetail(@PathVariable Long id) {
        return analysisService.getAnalysisDetail(id);
    }
}
