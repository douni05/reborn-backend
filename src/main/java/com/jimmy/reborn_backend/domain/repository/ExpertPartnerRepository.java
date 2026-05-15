package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.ExpertPartner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertPartnerRepository extends JpaRepository<ExpertPartner, Long> {
    boolean existsByMember_UserId(Long userId);
    java.util.Optional<ExpertPartner> findByMember_UserId(Long userId);
}