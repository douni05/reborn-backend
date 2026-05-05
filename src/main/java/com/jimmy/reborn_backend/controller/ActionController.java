package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.service.XpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/action")
@RequiredArgsConstructor
public class ActionController {

    private final XpService xpService;

    @PostMapping("/reform/{userId}")
    public Map<String, Object> completeReform(@PathVariable Long userId) {
        int newXp = xpService.addXpForReform(userId);
        return Map.of("message", "리폼 완료!", "earnedXp", 50, "totalXp", newXp);
    }

    @PostMapping("/disposal/{userId}")
    public Map<String, Object> completeDisposal(@PathVariable Long userId) {
        int newXp = xpService.addXpForDisposal(userId);
        return Map.of("message", "분리배출 완료!", "earnedXp", 30, "totalXp", newXp);
    }
}