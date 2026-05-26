package com.jimmy.reborn_backend.domain.entity;

import com.jimmy.reborn_backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ServiceMatch extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private ExpertPartner expertPartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private ReformPlan reformPlan;

    private String status; // REQUEST, WORKING, COMPLETED
}