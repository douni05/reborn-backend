package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}