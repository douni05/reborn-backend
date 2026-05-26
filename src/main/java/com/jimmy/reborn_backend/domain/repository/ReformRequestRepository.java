package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.ReformRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReformRequestRepository extends JpaRepository<ReformRequest, Long> {
    List<ReformRequest> findByRequester_UserIdOrderByCreatedAtDesc(Long userId);
    List<ReformRequest> findByExpert_ShopIdOrderByCreatedAtDesc(Long shopId);
    boolean existsByRequester_UserIdAndExpert_ShopIdAndStatusIn(Long userId, Long shopId, java.util.List<com.jimmy.reborn_backend.domain.enums.RequestStatus> statuses);
}
