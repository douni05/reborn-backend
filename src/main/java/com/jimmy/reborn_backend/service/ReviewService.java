package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.ExpertPartner;
import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.entity.ReformRequest;
import com.jimmy.reborn_backend.domain.entity.Review;
import com.jimmy.reborn_backend.domain.repository.ExpertPartnerRepository;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import com.jimmy.reborn_backend.domain.repository.ReformRequestRepository;
import com.jimmy.reborn_backend.domain.repository.ReviewRepository;
import com.jimmy.reborn_backend.dto.ReviewCreateDto;
import com.jimmy.reborn_backend.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ExpertPartnerRepository expertPartnerRepository;
    private final ReformRequestRepository reformRequestRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Transactional
    public void createReview(Long userId, ReviewCreateDto dto) {
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("별점은 1~5 사이로 입력해주세요");
        }
        if (reviewRepository.existsByReformRequest_RequestId(dto.getRequestId())) {
            throw new IllegalArgumentException("이미 리뷰를 작성했어요");
        }
        Member reviewer = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없어요"));
        ExpertPartner expert = expertPartnerRepository.findById(dto.getShopId())
                .orElseThrow(() -> new IllegalArgumentException("전문가를 찾을 수 없어요"));
        ReformRequest request = reformRequestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없어요"));

        reviewRepository.save(Review.builder()
                .reviewer(reviewer)
                .expert(expert)
                .reformRequest(request)
                .rating(dto.getRating())
                .content(dto.getContent())
                .build());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getShopReviews(Long shopId) {
        List<Review> reviews = reviewRepository.findByExpert_ShopIdOrderByCreatedAtDesc(shopId);
        Double avg = reviewRepository.getAverageRatingByShopId(shopId);
        long count = reviewRepository.countByExpert_ShopId(shopId);

        List<ReviewResponseDto> list = reviews.stream()
                .map(r -> ReviewResponseDto.builder()
                        .reviewId(r.getReviewId())
                        .reviewerNickname(r.getReviewer().getNickname())
                        .rating(r.getRating())
                        .content(r.getContent())
                        .createdAt(r.getCreatedAt() != null ? r.getCreatedAt().format(FORMATTER) : "")
                        .build())
                .collect(Collectors.toList());

        return Map.of(
                "averageRating", avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0,
                "reviewCount", count,
                "reviews", list
        );
    }

    @Transactional(readOnly = true)
    public boolean hasReview(Long requestId) {
        return reviewRepository.existsByReformRequest_RequestId(requestId);
    }
}
