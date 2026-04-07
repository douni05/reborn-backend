package com.jimmy.reborn_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class Achievement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementId;

    @Column(unique = true)
    private Integer levelThreshold;

    private String titleName;
    private String iconUrl;
}