package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import com.jimmy.reborn_backend.dto.MemberRequestDto;
import com.jimmy.reborn_backend.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponseDto join(MemberRequestDto dto) {
        // 닉네임 중복 체크
        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임이에요.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .role(dto.getRole() != null ? dto.getRole() : "USER")
                .build();

        Member saved = memberRepository.save(member);

        return MemberResponseDto.builder()
                .userId(saved.getUserId())
                .nickname(saved.getNickname())
                .totalXp(saved.getTotalXp())
                .currentLevel(saved.getCurrentLevel())
                .build();
    }

    public MemberResponseDto getProfile(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요."));

        return MemberResponseDto.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .totalXp(member.getTotalXp())
                .currentLevel(member.getCurrentLevel())
                .totalReformCount(member.getTotalReformCount())
                .totalDisposalCount(member.getTotalDisposalCount())
                .build();
    }
}