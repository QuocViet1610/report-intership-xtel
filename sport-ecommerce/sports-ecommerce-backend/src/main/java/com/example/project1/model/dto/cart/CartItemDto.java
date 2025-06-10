package com.example.project1.model.dto.cart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    private Long id;

    private Long productId;

    private Long productVariantId;

    private Long quantity;


//    private BigDecimal price;
//    private BigDecimal total;
}
