package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 사용자 찾기 기능 추가
    Optional<Member> findByEmail(String email);
    // 닉네임 중복 확인 기능 추가
    boolean existsByNickname(String nickname);
}