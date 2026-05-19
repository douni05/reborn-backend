package com.jimmy.reborn_backend.controller;

import com.jimmy.reborn_backend.dto.ReviewCreateDto;
import com.jimmy.reborn_backend.dto.ShopReviewResponseDto;
import com.jimmy.reborn_backend.global.jwt.JwtUtil;
import com.jimmy.reborn_backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Void> createReview(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ReviewCreateDto dto) {
        Long userId = jwtUtil.getUserId(authorization.replace("Bearer ", ""));
        reviewService.createReview(userId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shop/{shopId}")
    public ShopReviewResponseDto getShopReviews(@PathVariable Long shopId) {
        return reviewService.getShopReviews(shopId);
    }

    @GetMapping("/check/{requestId}")
    public Map<String, Boolean> hasReview(@PathVariable Long requestId) {
        return Map.of("hasReview", reviewService.hasReview(requestId));
    }
}
