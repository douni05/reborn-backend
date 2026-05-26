package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.DisposalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisposalHistoryRepository extends JpaRepository<DisposalHistory, Long> {
}