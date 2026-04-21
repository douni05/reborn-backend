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

        // 2. FastAPI 서버로 분석 요청 (실제 FastAPI 주소 필요)
        // String fastapiUrl = "http://localhost:8000/analyze";
        // AIResult aiResult = restTemplate.postForObject(fastapiUrl, dto, AIResult.class);

        // 3. (테스트용) 가짜 AI 분석 결과 생성
        boolean isReformable = true; // AI가 판단했다고 가정

        // 4. 분석 이력 DB 저장
        AnalysisHistory history = AnalysisHistory.builder()
                .member(member)
                .originImgUrl(dto.getOriginImgUrl())
                .materialType("청바지") // AI 결과값
                .conditionGrade("A")    // AI 결과값
                .isReformable(isReformable)
                .build();

        AnalysisHistory savedHistory = analysisHistoryRepository.save(history);

        // 5. 결과 반환
        return AnalysisResponseDto.builder()
                .analysisId(savedHistory.getAnalysisId())
                .materialType(savedHistory.getMaterialType())
                .conditionGrade(savedHistory.getConditionGrade())
                .isReformable(savedHistory.getIsReformable())
                .build();
    }
}