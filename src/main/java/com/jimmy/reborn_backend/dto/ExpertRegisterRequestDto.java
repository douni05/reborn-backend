package com.jimmy.reborn_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExpertRegisterRequestDto {
    private String shopName;
    private String businessNumber;
    private String ownerName;
    private String phone;
    private String address;
    private String detailAddress;
    private String category;
    private String introduction;
    private String imageUrl;
}
