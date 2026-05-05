package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.AnalysisHistory;
import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.repository.AnalysisHistoryRepository;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import com.jimmy.reborn_backend.dto.AnalysisRequestDto;
import com.jimmy.reborn_backend.dto.AnalysisResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisService {

    private final AnalysisHistoryRepository analysisHistoryRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public AnalysisResponseDto analyzeClothing(Long userId, AnalysisRequestDto dto) {
        // 1. 사용자 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 2. FastAPI 서버 주소 업데이트 (v2 엔드포인트)
        String fastapiUrl = "http://127.0.0.1:8000/analyze-v2";

        // 3. FastAPI로 보낼 데이터 구성 (안드로이드에서 받은 라벨 전달)
        // FastAPI의 LabelRequest가 {"label": "..."} 형식을 기다리므로 Map을 활용해 맞춰줍니다.
        Map<String, String> requestBody = Map.of("label", dto.getLabel());

        // 4. FastAPI 호출 및 결과 수신
        Map<String, Object> aiResult = restTemplate.postForObject(fastapiUrl, requestBody, Map.class);

        // 5. FastAPI(Gemini)가 준 데이터 꺼내기
        String materialType = (String) aiResult.get("label"); // 인식된 물체 이름
        String reformPlan = (String) aiResult.get("reformPlan"); // Gemini의 리폼 계획

        // 6. 분석 이력 DB 저장
        // Tip: AnalysisHistory 엔티티에 reformPlan(TEXT 타입) 필드가 없다면 추가해주는 것이 좋습니다!
        AnalysisHistory history = AnalysisHistory.builder()
                .member(member)
                .materialType(materialType)
                .reformPlan(reformPlan) // Gemini가 준 상세 가이드 저장
                .build();

        AnalysisHistory savedHistory = analysisHistoryRepository.save(history);

        // 7. 프론트엔드로 최종 결과 반환
        return AnalysisResponseDto.builder()
                .analysisId(savedHistory.getAnalysisId())
                .materialType(savedHistory.getMaterialType())
                .reformPlan(savedHistory.getReformPlan()) // 안드로이드에 리폼 계획 전달
                .build();
    }
}