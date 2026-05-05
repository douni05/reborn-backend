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

    private static final int XP_ANALYSIS = 10;
    private static final int XP_REFORM   = 50;
    private static final int XP_DISPOSAL = 30;

    @Transactional
    public int addXpForAnalysis(Long userId) {
        return addXp(userId, XP_ANALYSIS);
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

    private int addXp(Long userId, int xp) {
        Member member = find(userId);
        int newXp    = member.getTotalXp() + xp;
        int newLevel = Math.min((newXp / 200) + 1, 50);
        member.updateXpAndLevel(newXp, newLevel);
        return newXp;
    }

    private Member find(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 유저예요."));
    }
}