package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class XpService {

    private final MemberRepository memberRepository;

    public static final int XP_ANALYSIS       = 10;
    public static final int XP_ANALYSIS_FIRST = 50;
    public static final int XP_REFORM         = 100;
    public static final int XP_DISPOSAL       = 50;
    public static final int XP_MATCH          = 150;

    // 누적 XP 임계값 (index i → Lv.(i+1))
    static final int[] LEVEL_THRESHOLDS = {
        0,     // Lv.1
        50,    // Lv.2  — 첫 분석(+50) 시 바로 도달
        110,   // Lv.3
        180,   // Lv.4
        260,   // Lv.5
        360,   // Lv.6  — [칭호] 주니어 리포머
        470,   // Lv.7
        590,   // Lv.8
        720,   // Lv.9
        870,   // Lv.10
        1000,  // Lv.11
        1150,  // Lv.12
        1350,  // Lv.13
        1500,  // Lv.14
        1700,  // Lv.15
        1950,  // Lv.16 — [칭호] 프로 환경러
        2150,  // Lv.17
        2350,  // Lv.18
        2550,  // Lv.19
        2750,  // Lv.20
        2950,  // Lv.21
        3150,  // Lv.22
        3350,  // Lv.23
        3550,  // Lv.24
        3750,  // Lv.25
        3950,  // Lv.26
        4150,  // Lv.27
        4350,  // Lv.28
        4500,  // Lv.29
        5000,  // Lv.30
        5600,  // Lv.31 — [칭호] 에코 마스터
        5800,  // Lv.32
        6000,  // Lv.33
        6200,  // Lv.34
        6400,  // Lv.35
        6600,  // Lv.36
        6800,  // Lv.37
        7000,  // Lv.38
        7200,  // Lv.39
        7400,  // Lv.40
        7600,  // Lv.41
        7800,  // Lv.42
        8000,  // Lv.43
        8200,  // Lv.44
        8400,  // Lv.45
        8600,  // Lv.46
        8800,  // Lv.47
        9000,  // Lv.48
        9200,  // Lv.49
        10000  // Lv.50 — [칭호] 지구 수호자
    };

    @Transactional
    public int addXpForAnalysis(Long userId, boolean isFirst) {
        return addXp(userId, isFirst ? XP_ANALYSIS_FIRST : XP_ANALYSIS);
    }

    @Transactional
    public int addXpForReform(Long userId) {
        Member member = find(userId);
        member.incrementReformCount();
        return addXp(userId, XP_REFORM);
    }

    @Transactional
    public int addXpForDisposal(Long userId) {
        Member member = find(userId);
        member.incrementDisposalCount();
        return addXp(userId, XP_DISPOSAL);
    }

    @Transactional
    public int addXpForMatch(Long userId) {
        return addXp(userId, XP_MATCH);
    }

    private int addXp(Long userId, int xp) {
        Member member = find(userId);
        int newXp = member.getTotalXp() + xp;
        int newLevel = computeLevel(newXp);
        member.updateXpAndLevel(newXp, newLevel);
        return newXp;
    }

    public static int computeLevel(int totalXp) {
        for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
            if (totalXp >= LEVEL_THRESHOLDS[i]) {
                return i + 1;
            }
        }
        return 1;
    }

    public static String getTitleForLevel(int level) {
        if (level >= 50) return "지구 수호자";
        if (level >= 31) return "에코 마스터";
        if (level >= 16) return "프로 환경러";
        if (level >= 6)  return "주니어 리포머";
        return "새싹 지구 지킴이";
    }

    private Member find(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요."));
    }
}
