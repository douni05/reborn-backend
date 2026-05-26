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

import java.util.HashMap;
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

    @org.springframework.beans.factory.annotation.Value("${ai.server.url:http://localhost:8000}")
    private String aiServerUrl;

    @Transactional
    public AnalysisResponseDto analyzeClothing(Long userId, AnalysisRequestDto dto) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요. id=" + userId));

        FastApiResponseDto aiResult = callFastApi(dto.getLabel(), dto.getImageBase64());

        String materialType = aiResult.getMaterialType() != null
                ? aiResult.getMaterialType().toLowerCase()
                : "";

        // exact match만 사용 (default 폴백 없이)
        DisposalGuide exactGuide = disposalGuideRepository
                .findByMaterialType(materialType)
                .orElse(null);

        // 우선순위: ① DB exact match → ② AI disposalGuide → ③ DB default → ④ 하드코딩 폴백
        String disposalIcon;
        String disposalMethod;
        boolean hasExactGuide = exactGuide != null
                && exactGuide.getDischargeMethod() != null
                && !exactGuide.getDischargeMethod().isBlank();

        if (hasExactGuide) {
            disposalIcon = exactGuide.getCategoryIcon() != null ? exactGuide.getCategoryIcon() : "🗑️";
            disposalMethod = exactGuide.getDischargeMethod();
            log.info("DB exact match 사용: materialType={}", materialType);
        } else if (aiResult.getDisposalGuide() != null && !aiResult.getDisposalGuide().isBlank()) {
            disposalIcon = "🗑️";
            disposalMethod = aiResult.getDisposalGuide();
            log.info("AI disposalGuide 사용: materialType={}", materialType);
        } else {
            DisposalGuide defaultGuide = disposalGuideRepository.findByMaterialType("default").orElse(null);
            disposalIcon = defaultGuide != null ? defaultGuide.getCategoryIcon() : "🗑️";
            disposalMethod = defaultGuide != null && defaultGuide.getDischargeMethod() != null
                    ? defaultGuide.getDischargeMethod()
                    : "step1: 재질을 확인하세요\nstep2: 해당 분리배출함에 배출하세요";
            log.info("DB default/hardcoded 폴백 사용: materialType={}", materialType);
        }

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
                .disposalIcon(disposalIcon)
                .disposalMethod(disposalMethod)
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

        boolean isFirst = analysisHistoryRepository.countByMember_UserId(userId) == 1;
        xpService.addXpForAnalysis(userId, isFirst);

        return AnalysisResponseDto.builder()
                .analysisId(saved.getAnalysisId())
                .label(dto.getLabel())
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
                .disposalIcon(disposalIcon)
                .disposalMethod(disposalMethod)
                .build();
    }

    private static final java.time.format.DateTimeFormatter HISTORY_FMT =
            java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public List<AnalysisResponseDto> getHistory(Long userId) {
        return analysisHistoryRepository.findAllByMember_UserIdOrderByCreatedAtDesc(userId)
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
                            .materials(h.getMaterials())
                            .estimatedTime(h.getEstimatedTime())
                            .estimatedCost(h.getEstimatedCost())
                            .createdAt(h.getCreatedAt() != null
                                    ? h.getCreatedAt().format(HISTORY_FMT) : "")
                            .isDisposalCompleted(Boolean.TRUE.equals(h.getIsDisposalCompleted()))
                            .isReformVerified(Boolean.TRUE.equals(h.getIsReformVerified()))
                            .build();
                })
                .toList();
    }

    public AnalysisResponseDto getAnalysisDetail(Long analysisId) {
        AnalysisHistory h = analysisHistoryRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분석 결과예요. id=" + analysisId));

        boolean isReformable = Boolean.TRUE.equals(h.getIsReformable());

        Long planId = null;

        if (isReformable) {
            planId = reformPlanRepository.findByAnalysis_AnalysisId(analysisId)
                    .map(ReformPlan::getPlanId)
                    .orElse(null);
        }

        // 저장된 배출 정보 우선 사용, 없으면 DB 재조회
        String disposalIcon;
        String disposalMethod;
        if (h.getDisposalMethod() != null && !h.getDisposalMethod().isBlank()) {
            disposalIcon = h.getDisposalIcon() != null ? h.getDisposalIcon() : "🗑️";
            disposalMethod = h.getDisposalMethod();
        } else {
            // 구버전 데이터 호환: DB에서 재조회
            String materialTypeLower = h.getMaterialType() != null ? h.getMaterialType().toLowerCase() : "";
            DisposalGuide exactGuide = disposalGuideRepository.findByMaterialType(materialTypeLower).orElse(null);
            boolean hasExactGuide = exactGuide != null
                    && exactGuide.getDischargeMethod() != null
                    && !exactGuide.getDischargeMethod().isBlank();
            if (hasExactGuide) {
                disposalIcon = exactGuide.getCategoryIcon() != null ? exactGuide.getCategoryIcon() : "🗑️";
                disposalMethod = exactGuide.getDischargeMethod();
            } else {
                disposalIcon = "🗑️";
                disposalMethod = "step1: 재질을 확인하세요\nstep2: 해당 재질의 분리배출함에 배출하세요\nstep3: 이물질이 묻어있다면 세척 후 배출하세요";
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
                .isDisposalCompleted(Boolean.TRUE.equals(h.getIsDisposalCompleted()))
                .isReformVerified(Boolean.TRUE.equals(h.getIsReformVerified()))
                .build();
    }

    private FastApiResponseDto callFastApi(String label, String imageBase64) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("label", label);
            if (imageBase64 != null && !imageBase64.isBlank()) {
                body.put("imageBase64", imageBase64);
            }
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiResponseDto> response = restTemplate.exchange(
                    aiServerUrl + "/analyze-v2", HttpMethod.POST, request, FastApiResponseDto.class);

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
