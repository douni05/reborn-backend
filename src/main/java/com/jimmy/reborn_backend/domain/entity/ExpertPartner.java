package com.jimmy.reborn_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expert_partners")
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ExpertPartner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    private String shopName;
    private String businessNumber;
    private String ownerName;
    private String phone;
    private String address;
    private String detailAddress;
    private String category;

    @Column(length = 1000)
    private String introduction;

    private String imageUrl;

    @Column(precision = 13, scale = 10)
    private BigDecimal latitude;
    @Column(precision = 13, scale = 10)
    private BigDecimal longitude;

    public void update(com.jimmy.reborn_backend.dto.ExpertRegisterRequestDto dto) {
        this.shopName = dto.getShopName();
        this.businessNumber = dto.getBusinessNumber();
        this.ownerName = dto.getOwnerName();
        this.phone = dto.getPhone();
        this.address = dto.getAddress();
        this.detailAddress = dto.getDetailAddress();
        this.category = dto.getCategory();
        this.introduction = dto.getIntroduction();
        if (dto.getImageUrl() != null) {
            this.imageUrl = dto.getImageUrl();
        }
        if (dto.getLatitude() != null) {
            this.latitude = java.math.BigDecimal.valueOf(dto.getLatitude());
        }
        if (dto.getLongitude() != null) {
            this.longitude = java.math.BigDecimal.valueOf(dto.getLongitude());
        }
    }
}