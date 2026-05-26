package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.ReformRequestActionDto;
import com.jimmy.reborn_backend.dto.ReformRequestCreateDto;
import com.jimmy.reborn_backend.dto.ReformRequestDto;
import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.ReformRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reform-requests")
@RequiredArgsConstructor
public class ReformRequestController {

    private final ReformRequestService reformRequestService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ReformRequestDto createRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ReformRequestCreateDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return reformRequestService.createRequest(userId, dto);
    }

    @GetMapping("/my")
    public List<ReformRequestDto> getMyRequests(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return reformRequestService.getMyRequests(userId);
    }

    @GetMapping("/expert")
    public List<ReformRequestDto> getExpertRequests(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return reformRequestService.getExpertRequests(userId);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody ReformRequestActionDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        reformRequestService.acceptRequest(userId, id, dto.getMessage());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody ReformRequestActionDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        reformRequestService.rejectRequest(userId, id, dto.getMessage());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeRequest(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        reformRequestService.completeRequest(userId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public Map<String, Boolean> hasActiveRequest(
            @RequestHeader("Authorization") String authorization,
            @RequestParam Long shopId) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return Map.of("hasActive", reformRequestService.hasActiveRequest(userId, shopId));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRequest(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        reformRequestService.cancelRequest(userId, id);
        return ResponseEntity.ok().build();
    }
}
