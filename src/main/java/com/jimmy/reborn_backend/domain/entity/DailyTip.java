package com.jimmy.reborn_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_tips")
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class DailyTip {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private LocalDate tipDate;
}
