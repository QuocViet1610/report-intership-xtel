package com.example.project1.model.dto.Order;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AddressDto {
    private Long id;

    private Long userId;

    private String fullName;

    private String phoneNumber;

    private String provinceName;

    private Long provinceId;

    private String districtName;

    private Long districtId;

    private String wardName;

    private Long wardId;

    private String country;

    private Integer isDefault;

    private String note;

    private String addressText;

    private String addressType;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;
}
