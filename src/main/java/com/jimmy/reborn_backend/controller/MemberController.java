package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.MemberRequestDto;
import com.jimmy.reborn_backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public String join(@RequestBody MemberRequestDto dto) {
        Long userId = memberService.join(dto);
        return "회원가입 성공! ID: " + userId;
    }
}