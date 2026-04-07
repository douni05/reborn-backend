package com.jimmy.reborn_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ExpertPartner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    private String shopName;
    private String category;

    @Column(precision = 13, scale = 10)
    private BigDecimal latitude;
    @Column(precision = 13, scale = 10)
    private BigDecimal longitude;
}