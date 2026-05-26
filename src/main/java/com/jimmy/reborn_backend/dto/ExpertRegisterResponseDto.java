package com.jimmy.reborn_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpertRegisterResponseDto {
    private Long shopId;
    private String shopName;
    private String category;
    private String address;
    private String detailAddress;
    private String introduction;
    private String imageUrl;
}
