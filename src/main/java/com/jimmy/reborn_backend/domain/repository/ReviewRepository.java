package com.jimmy.reborn_backend.domain.repository;

import com.jimmy.reborn_backend.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByExpert_ShopIdOrderByCreatedAtDesc(Long shopId);
    boolean existsByReformRequest_RequestId(Long requestId);
    long countByExpert_ShopId(Long shopId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.expert.shopId = :shopId")
    Double getAverageRatingByShopId(@Param("shopId") Long shopId);
}
