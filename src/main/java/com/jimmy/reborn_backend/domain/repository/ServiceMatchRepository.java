package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.ServiceMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceMatchRepository extends JpaRepository<ServiceMatch, Long> {
}