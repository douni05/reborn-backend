package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.DisposalGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DisposalGuideRepository extends JpaRepository<DisposalGuide, Long> {
    // 재질 타입으로 가이드 찾기 기능 추가
    Optional<DisposalGuide> findByMaterialType(String materialType);
}