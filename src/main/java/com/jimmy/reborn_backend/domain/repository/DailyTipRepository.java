package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.DailyTip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyTipRepository extends JpaRepository<DailyTip, Long> {
    Optional<DailyTip> findByTipDate(LocalDate tipDate);
}
