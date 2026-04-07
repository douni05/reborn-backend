package com.jimmy.reborn_backend.domain.entity;

import com.jimmy.reborn_backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class AnalysisHistory extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analysisId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    private String originImgUrl;
    private String materialType;
    private String conditionGrade;
    private Boolean isReformable;
}