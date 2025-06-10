package com.example.project1.model.dto.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CouponDto {
    private Long couponId;

    private String name;

    private String couponCode;

    private BigDecimal discountValue;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean isActive = true;

    private int usageLimit = 1;

    private int timesUsed = 0;
}
