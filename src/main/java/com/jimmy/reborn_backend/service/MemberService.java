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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AchievementRepository achievementRepository;
    private final JwtUtil jwtUtil;

    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void updateTitle(Long userId, String titleName) {
        if (titleName == null || titleName.isBlank()) {
            throw new IllegalArgumentException("칭호를 입력해주세요.");
        }
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요."));
        member.updateTitle(titleName);
    }

    @Transactional
    public void updateNickname(Long userId, String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        }
        if (nickname.length() < 2 || nickname.length() > 8) {
            throw new IllegalArgumentException("닉네임은 2자 이상 8자 이하로 입력해주세요.");
        }
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임이에요.");
        }
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요."));
        member.updateNickname(nickname);
    }

    @Transactional
    public void withdraw(Long userId) {
        memberRepository.deleteById(userId);
    }


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

        // 저장된 칭호 우선, 없으면 레벨 기반 기본 칭호
        String titleName = (member.getTitleName() != null && !member.getTitleName().isBlank())
                ? member.getTitleName()
                : XpService.getTitleForLevel(member.getCurrentLevel());

        // 레벨 기반 업적 (달성한 칭호 전환 마일스톤)
        List<AchievementResponseDto> achievements = new ArrayList<>(
                achievementRepository.findAll()
                        .stream()
                        .filter(a -> a.getLevelThreshold() <= member.getCurrentLevel())
                        .map(this::toAchievementDto)
                        .toList()
        );

        // 특별 업적 (조건 기반 동적 체크)
        if (member.getTotalReformCount() >= 10) {
            achievements.add(AchievementResponseDto.builder()
                    .titleName("패션 아이콘")
                    .iconUrl("🏆")
                    .build());
        }
        if (member.getTotalReformCount() >= 5) {
            achievements.add(AchievementResponseDto.builder()
                    .titleName("맥가이버")
                    .iconUrl("⚒️")
                    .build());
        }
        if (member.getTotalExpertConnectionCount() >= 3) {
            achievements.add(AchievementResponseDto.builder()
                    .titleName("공방 단골손님")
                    .iconUrl("🤝")
                    .build());
        }
        if (member.getTotalDisposalCount() >= 5) {
            achievements.add(AchievementResponseDto.builder()
                    .titleName("분리배출의 신")
                    .iconUrl("📍")
                    .build());
        }

        return MemberResponseDto.builder()
                .userId(member.getUserId())
                .nickname(member.getNickname())
                .totalXp(member.getTotalXp())
                .currentLevel(member.getCurrentLevel())
                .titleName(titleName)
                .totalReformCount(member.getTotalReformCount())
                .totalDisposalCount(member.getTotalDisposalCount())
                .expertConnectionCount(member.getTotalExpertConnectionCount())
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
