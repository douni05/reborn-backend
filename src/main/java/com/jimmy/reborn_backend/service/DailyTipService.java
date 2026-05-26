package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.DailyTip;
import com.jimmy.reborn_backend.domain.repository.DailyTipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTipService {

    private final DailyTipRepository dailyTipRepository;
    private final RestTemplate restTemplate;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    private static final String FALLBACK_TIP = "오늘 한 번만 더 분리배출 확인해보세요. 작은 습관이 지구를 바꿉니다!";

    public String getTodayTip() {
        return dailyTipRepository.findByTipDate(LocalDate.now())
                .map(DailyTip::getContent)
                .orElseGet(this::fetchAndSave);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void scheduledFetch() {
        if (dailyTipRepository.findByTipDate(LocalDate.now()).isEmpty()) {
            fetchAndSave();
        }
    }

    @Transactional
    public String fetchAndSave() {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> response = restTemplate.getForObject(
                    aiServerUrl + "/daily-tip", Map.class);

            String tip = (response != null && response.containsKey("tip"))
                    ? response.get("tip")
                    : FALLBACK_TIP;

            dailyTipRepository.save(DailyTip.builder()
                    .content(tip)
                    .tipDate(LocalDate.now())
                    .build());

            return tip;
        } catch (Exception e) {
            log.warn("오늘의 팁 생성 실패, 기본 팁 사용: {}", e.getMessage());
            return FALLBACK_TIP;
        }
    }
}
