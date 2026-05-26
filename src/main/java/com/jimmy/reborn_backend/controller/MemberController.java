package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.JoinResponseDto;
import com.jimmy.reborn_backend.dto.MemberRequestDto;
import com.jimmy.reborn_backend.dto.MemberResponseDto;
import com.jimmy.reborn_backend.dto.NicknameUpdateDto;
import com.jimmy.reborn_backend.dto.TitleUpdateDto;
import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @GetMapping("/check-email")
    public ResponseEntity<Void> checkEmail(@RequestParam String email) {
        boolean exists = memberService.existsByEmail(email);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/join")
    public JoinResponseDto join(@RequestBody MemberRequestDto dto) {
        return memberService.join(dto);
    }

    @GetMapping("/me")
    public MemberResponseDto getMyProfile(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        return memberService.getProfile(userId);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(
            @RequestHeader("Authorization") String authorization,
            @RequestBody NicknameUpdateDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        memberService.updateNickname(userId, dto.getNickname());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/title")
    public ResponseEntity<Void> updateTitle(
            @RequestHeader("Authorization") String authorization,
            @RequestBody TitleUpdateDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        memberService.updateTitle(userId, dto.getTitleName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(
            @RequestHeader("Authorization") String authorization,
            @RequestBody Map<String, String> body) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        memberService.updateFcmToken(userId, body.get("fcmToken"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(
            @RequestHeader("Authorization") String authorization) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        memberService.withdraw(userId);
        return ResponseEntity.noContent().build();
    }
}
