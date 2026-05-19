package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.XpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/action")
@RequiredArgsConstructor
public class ActionController {

    private final XpService xpService;
    private final JwtUtil jwtUtil;

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
}
