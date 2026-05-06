package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.Achievement;
import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.repository.AchievementRepository;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import com.jimmy.reborn_backend.dto.AchievementResponseDto;
import com.jimmy.reborn_backend.dto.JoinResponseDto;
import com.jimmy.reborn_backend.dto.MemberRequestDto;
import com.jimmy.reborn_backend.dto.MemberResponseDto;
import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AchievementRepository achievementRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public JoinResponseDto join(MemberRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    if (memberRepository.existsByNickname(dto.getNickname())) {
                        throw new IllegalArgumentException("이미 사용 중인 닉네임이에요.");
                    }
                    return memberRepository.save(Member.builder()
                            .email(dto.getEmail())
                            .nickname(dto.getNickname())
                            .role(dto.getRole() != null ? dto.getRole() : "USER")
                            .build());
                });

        String token = jwtUtil.generateToken(member.getUserId(), member.getEmail());

        return JoinResponseDto.builder()
                .token(token)
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .totalXp(member.getTotalXp())
                .currentLevel(member.getCurrentLevel())
                .build();
    }

    public MemberResponseDto getProfile(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요."));

        List<AchievementResponseDto> achievements = achievementRepository.findAll()
                .stream()
                .filter(a -> a.getLevelThreshold() <= member.getCurrentLevel())
                .map(this::toAchievementDto)
                .toList();

        return MemberResponseDto.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .totalXp(member.getTotalXp())
                .currentLevel(member.getCurrentLevel())
                .totalReformCount(member.getTotalReformCount())
                .totalDisposalCount(member.getTotalDisposalCount())
                .achievements(achievements)
                .build();
    }

    private AchievementResponseDto toAchievementDto(Achievement a) {
        return AchievementResponseDto.builder()
                .achievementId(a.getAchievementId())
                .levelThreshold(a.getLevelThreshold())
                .titleName(a.getTitleName())
                .iconUrl(a.getIconUrl())
                .build();
    }
}
