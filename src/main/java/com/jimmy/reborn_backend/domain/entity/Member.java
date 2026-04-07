package com.jimmy.reborn_backend.domain.entity;

import com.jimmy.reborn_backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Builder.Default private Integer totalXp = 0;
    @Builder.Default private Integer currentLevel = 1;
    @Builder.Default private Integer totalReformCount = 0;
    @Builder.Default private Integer totalDisposalCount = 0;
    @Builder.Default private Float carbonReduction = 0.0f;
    private String role; // USER, EXPERT
}