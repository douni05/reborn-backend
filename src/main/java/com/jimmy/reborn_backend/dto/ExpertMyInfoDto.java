package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpertMyInfoDto {
    private Long shopId;
    private String shopName;
    private String businessNumber;
    private String ownerName;
    private String phone;
    private String address;
    private String detailAddress;
    private String category;
    private String introduction;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
}
