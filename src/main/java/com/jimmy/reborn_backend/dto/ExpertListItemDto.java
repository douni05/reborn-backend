package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpertListItemDto {
    private Long shopId;
    private String shopName;
    private String category;
    private String address;
    private String detailAddress;
    private String introduction;
    private String imageUrl;
    private String phone;
    private Double averageRating;
    private Long reviewCount;
    private Double latitude;
    private Double longitude;
}
