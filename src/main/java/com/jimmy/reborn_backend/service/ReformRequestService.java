package com.jimmy.reborn_backend.service;

import com.jimmy.reborn_backend.domain.entity.ExpertPartner;
import com.jimmy.reborn_backend.domain.entity.Member;
import com.jimmy.reborn_backend.domain.entity.ReformRequest;
import com.jimmy.reborn_backend.domain.entity.ServiceMatch;
import com.jimmy.reborn_backend.domain.repository.ExpertPartnerRepository;
import com.jimmy.reborn_backend.domain.repository.MemberRepository;
import com.jimmy.reborn_backend.domain.repository.ReformRequestRepository;
import com.jimmy.reborn_backend.domain.repository.ServiceMatchRepository;
import com.jimmy.reborn_backend.domain.enums.RequestStatus;
import com.jimmy.reborn_backend.dto.ReformRequestCreateDto;
import com.jimmy.reborn_backend.dto.ReformRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReformRequestService {

    private final ReformRequestRepository reformRequestRepository;
    private final MemberRepository memberRepository;
    private final ExpertPartnerRepository expertPartnerRepository;
    private final ServiceMatchRepository serviceMatchRepository;
    private final XpService xpService;
    private final FcmService fcmService;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

    @Transactional
    public ReformRequestDto createRequest(Long userId, ReformRequestCreateDto dto) {
        Member requester = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없어요"));
        ExpertPartner expert = expertPartnerRepository.findById(dto.getShopId())
                .orElseThrow(() -> new IllegalArgumentException("전문가를 찾을 수 없어요"));

        ReformRequest request = reformRequestRepository.save(ReformRequest.builder()
                .requester(requester)
                .expert(expert)
                .designTitle(dto.getDesignTitle())
                .requestContent(dto.getRequestContent())
                .planId(dto.getPlanId())
                .reformPlan(dto.getReformPlan())
                .difficulty(dto.getDifficulty())
                .materials(dto.getMaterials())
                .estimatedTime(dto.getEstimatedTime())
                .estimatedCost(dto.getEstimatedCost())
                .build());

        return toDto(request);
    }

    @Transactional(readOnly = true)
    public List<ReformRequestDto> getMyRequests(Long userId) {
        return reformRequestRepository.findByRequester_UserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReformRequestDto> getExpertRequests(Long userId) {
        ExpertPartner expert = expertPartnerRepository.findByMember_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("전문가로 등록되어 있지 않아요"));
        return reformRequestRepository.findByExpert_ShopIdOrderByCreatedAtDesc(expert.getShopId())
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void acceptRequest(Long expertUserId, Long requestId, String message) {
        ReformRequest request = getVerifiedRequest(expertUserId, requestId);
        request.accept(message);

        // 알림 전송
        String fcmToken = request.getRequester().getFcmToken();
        fcmService.sendNotification(fcmToken, "리폼 요청 수락 🎉", "전문가가 리폼 요청을 수락했어요!");
    }

    @Transactional
    public void rejectRequest(Long expertUserId, Long requestId, String message) {
        ReformRequest request = getVerifiedRequest(expertUserId, requestId);
        request.reject(message);

        // 알림 전송
        String fcmToken = request.getRequester().getFcmToken();
        fcmService.sendNotification(fcmToken, "리폼 요청 거절", "전문가가 리폼 요청을 거절했어요");
    }

    @Transactional
    public void completeRequest(Long expertUserId, Long requestId) {
        ReformRequest request = getVerifiedRequest(expertUserId, requestId);
        request.complete();

        Member requester = request.getRequester();
        xpService.addXpForMatch(requester.getUserId());
        requester.incrementExpertConnectionCount();

        serviceMatchRepository.save(ServiceMatch.builder()
                .member(requester)
                .expertPartner(request.getExpert())
                .status("COMPLETED")
                .build());

        // 알림 전송
        fcmService.sendNotification(requester.getFcmToken(), "리폼 완료 ✅", "리폼이 완료되었어요! +150 XP 획득!");
    }

    private ReformRequest getVerifiedRequest(Long expertUserId, Long requestId) {
        ExpertPartner expert = expertPartnerRepository.findByMember_UserId(expertUserId)
                .orElseThrow(() -> new IllegalArgumentException("전문가로 등록되어 있지 않아요"));
        ReformRequest request = reformRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없어요"));
        if (!request.getExpert().getShopId().equals(expert.getShopId())) {
            throw new IllegalArgumentException("권한이 없습니다");
        }
        return request;
    }

    @Transactional(readOnly = true)
    public boolean hasActiveRequest(Long userId, Long shopId) {
        return reformRequestRepository.existsByRequester_UserIdAndExpert_ShopIdAndStatusIn(
            userId, shopId,
            java.util.List.of(RequestStatus.PENDING, RequestStatus.ACCEPTED)
        );
    }

    @Transactional
    public void cancelRequest(Long userId, Long requestId) {
        ReformRequest request = reformRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없어요"));
        if (!request.getRequester().getUserId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("대기 중인 요청만 취소할 수 있어요");
        }
        request.cancel();
    }

    private ReformRequestDto toDto(ReformRequest r) {
        return ReformRequestDto.builder()
                .requestId(r.getRequestId())
                .requesterNickname(r.getRequester().getNickname())
                .shopId(r.getExpert().getShopId())
                .shopName(r.getExpert().getShopName())
                .shopImageUrl(r.getExpert().getImageUrl())
                .designTitle(r.getDesignTitle())
                .requestContent(r.getRequestContent())
                .status(r.getStatus().name())
                .expertMessage(r.getExpertMessage())
                .createdAt(r.getCreatedAt() != null ? r.getCreatedAt().format(FORMATTER) : "")
                .planId(r.getPlanId())
                .reformPlan(r.getReformPlan())
                .difficulty(r.getDifficulty())
                .materials(r.getMaterials())
                .estimatedTime(r.getEstimatedTime())
                .estimatedCost(r.getEstimatedCost())
                .build();
    }
}
