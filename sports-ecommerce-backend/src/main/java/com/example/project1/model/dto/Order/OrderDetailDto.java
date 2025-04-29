package com.example.project1.model.dto.Order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailDto {
    private Long orderItemId;
    private Long orderId;
    private Long productId;
    private Long productVariantId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;
}
