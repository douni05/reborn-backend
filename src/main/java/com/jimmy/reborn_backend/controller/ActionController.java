package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.XpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/action")
@RequiredArgsConstructor
public class ActionController {

    private final XpService xpService;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Value("${ai.server.url:http://localhost:8000}")
    private String aiServerUrl;

    @PostMapping("/reform")
    public Map<String, Object> completeReform(@RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        int totalXp = xpService.addXpForReform(userId);
        return Map.of(
                "message", "리폼 완료! 폭죽 이펙트 🎉",
                "earnedXp", XpService.XP_REFORM,
                "totalXp", totalXp);
    }

    @PostMapping("/disposal")
    public Map<String, Object> completeDisposal(@RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        int totalXp = xpService.addXpForDisposal(userId);
        return Map.of(
                "message", "분리배출 완료! 체크 표시 ✅",
                "earnedXp", XpService.XP_DISPOSAL,
                "totalXp", totalXp);
    }

    @PostMapping("/match")
    public Map<String, Object> completeMatch(@RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        int totalXp = xpService.addXpForMatch(userId);
        return Map.of(
                "message", "전문가 매칭 완료! 로컬 상생 🤝",
                "earnedXp", XpService.XP_MATCH,
                "totalXp", totalXp);
    }

    @PostMapping("/reform-verify")
    public Map<String, Object> verifyReform(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, String> body) {

        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        String imageBase64 = body.get("imageBase64");
        String label = body.getOrDefault("label", "");

        if (imageBase64 == null || imageBase64.isBlank()) {
            return Map.of("isVerified", false, "message", "이미지가 없어요.", "earnedXp", 0, "totalXp", 0);
        }

        // FastAPI에 리폼 검증 요청
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> aiBody = new HashMap<>();
            aiBody.put("imageBase64", imageBase64);
            aiBody.put("label", label);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(aiBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    aiServerUrl + "/verify-reform", HttpMethod.POST, request, Map.class);

            Map<?, ?> aiResult = response.getBody();
            boolean isVerified = Boolean.TRUE.equals(aiResult != null ? aiResult.get("isVerified") : false);
            String message = aiResult != null ? (String) aiResult.get("message") : "검증 실패";

            if (isVerified) {
                int totalXp = xpService.addXpForReform(userId);
                return Map.of(
                        "isVerified", true,
                        "message", message,
                        "earnedXp", XpService.XP_REFORM,
                        "totalXp", totalXp);
            } else {
                return Map.of(
                        "isVerified", false,
                        "message", message,
                        "earnedXp", 0,
                        "totalXp", 0);
            }

        } catch (Exception e) {
            return Map.of(
                    "isVerified", false,
                    "message", "검증 중 오류가 발생했어요. 다시 시도해주세요.",
                    "earnedXp", 0,
                    "totalXp", 0);
        }
    }
}
