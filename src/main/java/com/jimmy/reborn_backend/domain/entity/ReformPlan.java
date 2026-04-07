package com.jimmy.reborn_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ReformPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id")
    private AnalysisHistory analysis;

    private String resultImgUrl;
    @Column(columnDefinition = "TEXT")
    private String guideJson;
    private String difficulty;
}