package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.DisposalGuideResponseDto;
import com.jimmy.reborn_backend.dto.ReformPlanResponseDto;
import com.jimmy.reborn_backend.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping("/reform/{id}")
    public ReformPlanResponseDto getReformPlan(@PathVariable Long id) {
        return solutionService.getReformPlan(id);
    }

    @GetMapping("/disposal")
    public List<DisposalGuideResponseDto> getDisposalGuides(
            @RequestParam(required = false) String materialType) {
        return solutionService.getDisposalGuides(materialType);
    }
}
