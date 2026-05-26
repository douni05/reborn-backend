package com.jimmy.reborn_backend.domain.entity;

import com.jimmy.reborn_backend.domain.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reform_requests")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReformRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private ExpertPartner expert;

    private String designTitle;

    @Column(length = 1000)
    private String requestContent;

    // 리폼 솔루션 정보 (사용자가 선택한 AI 분석 결과)
    private Long planId;

    @Column(length = 2000)
    private String reformPlan;

    private String difficulty;

    @Column(length = 500)
    private String materials;

    private String estimatedTime;
    private String estimatedCost;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(length = 500)
    private String expertMessage;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
    }

    public void accept(String message) {
        this.status = RequestStatus.ACCEPTED;
        this.expertMessage = message;
    }

    public void reject(String message) {
        this.status = RequestStatus.REJECTED;
        this.expertMessage = message;
    }

    public void complete() {
        this.status = RequestStatus.COMPLETED;
    }

    public void cancel() {
        this.status = RequestStatus.CANCELLED;
    }
}
