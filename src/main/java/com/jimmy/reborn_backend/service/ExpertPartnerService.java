package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.ExpertPartner;
import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.repository.ExpertPartnerRepository;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import com.jimmy.reborn_backend.domain.repository.ReviewRepository;
import com.jimmy.reborn_backend.dto.ExpertListItemDto;
import com.jimmy.reborn_backend.dto.ExpertMyInfoDto;
import com.jimmy.reborn_backend.dto.ExpertRegisterRequestDto;
import com.jimmy.reborn_backend.dto.ExpertRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertPartnerService {

    private final ExpertPartnerRepository expertPartnerRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ExpertRegisterResponseDto register(Long userId, ExpertRegisterRequestDto dto) {
        if (expertPartnerRepository.existsByMember_UserId(userId)) {
            throw new IllegalArgumentException("이미 전문가로 등록되어 있어요.");
        }

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저예요."));

        ExpertPartner expert = expertPartnerRepository.save(ExpertPartner.builder()
                .member(member)
                .shopName(dto.getShopName())
                .businessNumber(dto.getBusinessNumber())
                .ownerName(dto.getOwnerName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .category(dto.getCategory())
                .introduction(dto.getIntroduction())
                .imageUrl(dto.getImageUrl())
                .build());

        return ExpertRegisterResponseDto.builder()
                .shopId(expert.getShopId())
                .shopName(expert.getShopName())
                .category(expert.getCategory())
                .address(expert.getAddress())
                .detailAddress(expert.getDetailAddress())
                .introduction(expert.getIntroduction())
                .imageUrl(expert.getImageUrl())
                .build();
    }

    @Transactional(readOnly = true)
    public boolean isExpert(Long userId) {
        return expertPartnerRepository.existsByMember_UserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ExpertListItemDto> getAll() {
        return expertPartnerRepository.findAll().stream()
                .map(e -> ExpertListItemDto.builder()
                        .shopId(e.getShopId())
                        .shopName(e.getShopName())
                        .category(e.getCategory())
                        .address(e.getAddress())
                        .detailAddress(e.getDetailAddress())
                        .introduction(e.getIntroduction())
                        .imageUrl(e.getImageUrl())
                        .phone(e.getPhone())
                        .averageRating(reviewRepository.getAverageRatingByShopId(e.getShopId()))
                        .reviewCount(reviewRepository.countByExpert_ShopId(e.getShopId()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ExpertMyInfoDto getMyExpert(Long userId) {
        ExpertPartner expert = expertPartnerRepository.findByMember_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("전문가로 등록되어 있지 않아요."));
        return toMyInfoDto(expert);
    }

    @Transactional
    public ExpertMyInfoDto updateExpert(Long userId, ExpertRegisterRequestDto dto) {
        ExpertPartner expert = expertPartnerRepository.findByMember_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("전문가로 등록되어 있지 않아요."));
        expert.update(dto);
        return toMyInfoDto(expert);
    }

    @Transactional
    public void deleteExpert(Long userId) {
        ExpertPartner expert = expertPartnerRepository.findByMember_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("전문가로 등록되어 있지 않아요."));
        expertPartnerRepository.delete(expert);
    }

    private ExpertMyInfoDto toMyInfoDto(ExpertPartner e) {
        return ExpertMyInfoDto.builder()
                .shopId(e.getShopId())
                .shopName(e.getShopName())
                .businessNumber(e.getBusinessNumber())
                .ownerName(e.getOwnerName())
                .phone(e.getPhone())
                .address(e.getAddress())
                .detailAddress(e.getDetailAddress())
                .category(e.getCategory())
                .introduction(e.getIntroduction())
                .imageUrl(e.getImageUrl())
                .build();
    }
}
