package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.service.DailyTipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/tips")
@RequiredArgsConstructor
public class TipController {

    private final DailyTipService dailyTipService;

    @GetMapping("/today")
    public Map<String, String> getTodayTip() {
        return Map.of("tip", dailyTipService.getTodayTip());
    }
}
