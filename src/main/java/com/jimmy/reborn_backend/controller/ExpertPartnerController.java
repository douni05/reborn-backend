package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.ExpertListItemDto;
import com.jimmy.reborn_backend.dto.ExpertMyInfoDto;
import com.jimmy.reborn_backend.dto.ExpertRegisterRequestDto;
import com.jimmy.reborn_backend.dto.ExpertRegisterResponseDto;
import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.ExpertPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/experts")
@RequiredArgsConstructor
public class ExpertPartnerController {

    private final ExpertPartnerService expertPartnerService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ExpertRegisterResponseDto register(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ExpertRegisterRequestDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return expertPartnerService.register(userId, dto);
    }

    @GetMapping("/me")
    public Map<String, Boolean> isExpert(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return Map.of("isExpert", expertPartnerService.isExpert(userId));
    }

    @GetMapping
    public List<ExpertListItemDto> getAll() {
        return expertPartnerService.getAll();
    }

    @GetMapping("/my-info")
    public ExpertMyInfoDto getMyInfo(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return expertPartnerService.getMyExpert(userId);
    }

    @PutMapping("/my-info")
    public ExpertMyInfoDto updateMyInfo(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ExpertRegisterRequestDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return expertPartnerService.updateExpert(userId, dto);
    }

    @DeleteMapping("/my-info")
    public ResponseEntity<Void> deleteMyInfo(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        expertPartnerService.deleteExpert(userId);
        return ResponseEntity.noContent().build();
    }
}
