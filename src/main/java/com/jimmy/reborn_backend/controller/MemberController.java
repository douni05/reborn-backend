package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.MemberResponseDto;
import com.jimmy.reborn_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{userId}")
    public MemberResponseDto getProfile(@PathVariable Long userId) {
        return memberService.getProfile(userId);
    }
}