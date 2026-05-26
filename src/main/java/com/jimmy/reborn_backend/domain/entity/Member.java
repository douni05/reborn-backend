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
    @Builder.Default private Integer totalExpertConnectionCount = 0;
    private String role; // USER, EXPERT
    private String titleName; // 사용자가 선택한 칭호 (null이면 레벨 기반 기본 칭호)
    private String fcmToken;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateTitle(String titleName) {
        this.titleName = titleName;
    }

    public void updateXpAndLevel(int totalXp, int currentLevel) {
        this.totalXp = totalXp;
        this.currentLevel = currentLevel;
    }

    public void incrementReformCount() {
        this.totalReformCount += 1;
    }

    public void incrementDisposalCount() {
        this.totalDisposalCount += 1;
    }

    public void incrementExpertConnectionCount() {
        this.totalExpertConnectionCount += 1;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}

