package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.AnalysisHistory;
import com.jimmy.reborn_backend.domain.entity.DisposalGuide;
import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.entity.ReformPlan;
import com.jimmy.reborn_backend.domain.repository.AnalysisHistoryRepository;
import com.jimmy.reborn_backend.domain.repository.DisposalGuideRepository;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import com.jimmy.reborn_backend.domain.repository.ReformPlanRepository;
import com.jimmy.reborn_backend.dto.AnalysisRequestDto;
import com.jimmy.reborn_backend.dto.AnalysisResponseDto;
import com.jimmy.reborn_backend.dto.FastApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisService {

    private final AnalysisHistoryRepository analysisHistoryRepository;
    private final DisposalGuideRepository disposalGuideRepository;
    private final MemberRepository memberRepository;
    private final ReformPlanRepository reformPlanRepository;
    private final XpService xpService;
    private final RestTemplate restTemplate;

    private static final String FASTAPI_URL = "http://127.0.0.1:8000/analyze-v2";

    @Transactional
    public AnalysisResponseDto analyzeClothing(Long userId, AnalysisRequestDto dto) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요. id=" + userId));

        FastApiResponseDto aiResult = callFastApi(dto.getLabel());

        String materialType = aiResult.getMaterialType() != null
                ? aiResult.getMaterialType().toLowerCase()
                : "default";

        DisposalGuide guide = disposalGuideRepository
                .findByMaterialType(materialType)
                .orElseGet(() -> disposalGuideRepository.findByMaterialType("default").orElse(null));

        AnalysisHistory history = AnalysisHistory.builder()
                .member(member)
                .materialType(aiResult.getMaterialType())
                .conditionGrade(aiResult.getConditionGrade())
                .isReformable(aiResult.getIsReformable())
                .reformPlan(aiResult.getReformPlan())
                .reformTitle(aiResult.getReformTitle())
                .difficulty(aiResult.getDifficulty())
                .materials(aiResult.getMaterials())
                .estimatedTime(aiResult.getEstimatedTime())
                .estimatedCost(aiResult.getEstimatedCost())
                .build();

        AnalysisHistory saved = analysisHistoryRepository.save(history);

        boolean isReformable = Boolean.TRUE.equals(aiResult.getIsReformable());

        Long planId = null;
        if (isReformable) {
            ReformPlan plan = reformPlanRepository.save(ReformPlan.builder()
                    .analysis(saved)
                    .difficulty(aiResult.getDifficulty())
                    .guideJson(aiResult.getReformPlan())
                    .build());
            planId = plan.getPlanId();
        }

        xpService.addXpForAnalysis(userId);

        return AnalysisResponseDto.builder()
                .analysisId(saved.getAnalysisId())
                .materialType(aiResult.getMaterialType())
                .conditionGrade(aiResult.getConditionGrade())
                .isReformable(isReformable)
                .planId(isReformable ? planId : null)
                .reformTitle(isReformable ? aiResult.getReformTitle() : null)
                .reformPlan(isReformable ? aiResult.getReformPlan() : null)
                .difficulty(isReformable ? aiResult.getDifficulty() : null)
                .materials(isReformable ? aiResult.getMaterials() : null)
                .estimatedTime(isReformable ? aiResult.getEstimatedTime() : null)
                .estimatedCost(isReformable ? aiResult.getEstimatedCost() : null)
                .disposalIcon(!isReformable && guide != null ? guide.getCategoryIcon() : null)
                .disposalMethod(!isReformable && guide != null ? guide.getDischargeMethod() : null)
                .build();
    }

    public List<AnalysisResponseDto> getHistory(Long userId) {
        return analysisHistoryRepository.findAllByMember_UserId(userId)
                .stream()
                .map(h -> {
                    Long planId = reformPlanRepository.findByAnalysis_AnalysisId(h.getAnalysisId())
                            .map(ReformPlan::getPlanId)
                            .orElse(null);
                    return AnalysisResponseDto.builder()
                            .analysisId(h.getAnalysisId())
                            .materialType(h.getMaterialType())
                            .conditionGrade(h.getConditionGrade())
                            .isReformable(h.getIsReformable())
                            .planId(planId)
                            .reformTitle(h.getReformTitle())
                            .reformPlan(h.getReformPlan())
                            .difficulty(h.getDifficulty())
                            .build();
                })
                .toList();
    }

    public AnalysisResponseDto getAnalysisDetail(Long analysisId) {
        AnalysisHistory h = analysisHistoryRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분석 결과예요. id=" + analysisId));

        boolean isReformable = Boolean.TRUE.equals(h.getIsReformable());

        Long planId = null;
        String disposalIcon = null;
        String disposalMethod = null;

        if (isReformable) {
            planId = reformPlanRepository.findByAnalysis_AnalysisId(analysisId)
                    .map(ReformPlan::getPlanId)
                    .orElse(null);
        } else {
            String materialType = h.getMaterialType() != null
                    ? h.getMaterialType().toLowerCase()
                    : "default";
            DisposalGuide guide = disposalGuideRepository.findByMaterialType(materialType)
                    .orElseGet(() -> disposalGuideRepository.findByMaterialType("default").orElse(null));
            if (guide != null) {
                disposalIcon = guide.getCategoryIcon();
                disposalMethod = guide.getDischargeMethod();
            }
        }

        return AnalysisResponseDto.builder()
                .analysisId(h.getAnalysisId())
                .materialType(h.getMaterialType())
                .conditionGrade(h.getConditionGrade())
                .isReformable(isReformable)
                .planId(planId)
                .reformTitle(isReformable ? h.getReformTitle() : null)
                .reformPlan(isReformable ? h.getReformPlan() : null)
                .difficulty(isReformable ? h.getDifficulty() : null)
                .materials(isReformable ? h.getMaterials() : null)
                .estimatedTime(isReformable ? h.getEstimatedTime() : null)
                .estimatedCost(isReformable ? h.getEstimatedCost() : null)
                .disposalIcon(disposalIcon)
                .disposalMethod(disposalMethod)
                .build();
    }

    private FastApiResponseDto callFastApi(String label) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = Map.of("label", label);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiResponseDto> response = restTemplate.exchange(
                    FASTAPI_URL, HttpMethod.POST, request, FastApiResponseDto.class);

            log.info("FastAPI 응답: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.warn("FastAPI 호출 실패, fallback 사용. label={}, error={}", label, e.getMessage());
            return new FastApiResponseDto(
                    label, "알 수 없음", "B", false,
                    "step1: 재질을 확인하세요\nstep2: 해당 분리배출함에 배출하세요",
                    "Normal"
            );
        }
    }
}
