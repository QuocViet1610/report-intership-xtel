package com.example.project1.model.dto.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDto {
    private Long id;

    private Long userId;

    private Set<CartItemDto> cartItems;
}
