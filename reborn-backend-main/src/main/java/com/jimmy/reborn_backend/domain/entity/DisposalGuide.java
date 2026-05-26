package com.jimmy.reborn_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class DisposalGuide {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guideId;

    @Column(unique = true)
    private String materialType;

    private String categoryIcon;
    @Column(columnDefinition = "TEXT")
    private String dischargeMethod;
}