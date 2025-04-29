package com.example.project1.model.dto.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartCreateRequest {
    private Long id;

    private Long userId;

    private BigDecimal totalPrice;

    private Set<CartItemCreateRequest> cartItems;
}
