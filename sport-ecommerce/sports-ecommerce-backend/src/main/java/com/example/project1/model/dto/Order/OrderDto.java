package com.example.project1.model.dto.Order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private OffsetDateTime orderDate;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal finalPrice;
    private Long statusId;
    private String shippingAddress;
    private Long couponId;
    private Long totalProduct;
    private String paymentMethod;
    private String shippingMethod;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String vnpTxnRef;
    private String orderCode;

}
