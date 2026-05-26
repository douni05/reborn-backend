package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.DisposalGuide;
import com.jimmy.reborn_backend.domain.entity.ReformPlan;
import com.jimmy.reborn_backend.domain.repository.DisposalGuideRepository;
import com.jimmy.reborn_backend.domain.repository.ReformPlanRepository;
import com.jimmy.reborn_backend.dto.DisposalGuideResponseDto;
import com.jimmy.reborn_backend.dto.ReformPlanResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SolutionService {

    private final ReformPlanRepository reformPlanRepository;
    private final DisposalGuideRepository disposalGuideRepository;

    public ReformPlanResponseDto getReformPlan(Long planId) {
        ReformPlan plan = reformPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리폼 플랜이에요. id=" + planId));

        var analysis = plan.getAnalysis();
        return ReformPlanResponseDto.builder()
                .planId(plan.getPlanId())
                .analysisId(analysis.getAnalysisId())
                .reformTitle(analysis.getReformTitle())
                .difficulty(plan.getDifficulty())
                .materials(analysis.getMaterials())
                .estimatedTime(analysis.getEstimatedTime())
                .estimatedCost(analysis.getEstimatedCost())
                .guideJson(plan.getGuideJson())
                .resultImgUrl(plan.getResultImgUrl())
                .build();
    }

    public List<DisposalGuideResponseDto> getDisposalGuides(String materialType) {
        List<DisposalGuide> guides = materialType != null && !materialType.isBlank()
                ? disposalGuideRepository.findByMaterialType(materialType.toLowerCase())
                        .map(List::of).orElse(List.of())
                : disposalGuideRepository.findAll();

        return guides.stream()
                .map(g -> DisposalGuideResponseDto.builder()
                        .guideId(g.getGuideId())
                        .materialType(g.getMaterialType())
                        .categoryIcon(g.getCategoryIcon())
                        .dischargeMethod(g.getDischargeMethod())
                        .build())
                .toList();
    }
}
