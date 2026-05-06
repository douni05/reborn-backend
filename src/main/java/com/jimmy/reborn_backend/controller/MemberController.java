package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.JoinResponseDto;
import com.jimmy.reborn_backend.dto.MemberRequestDto;
import com.jimmy.reborn_backend.dto.MemberResponseDto;
import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

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
}
