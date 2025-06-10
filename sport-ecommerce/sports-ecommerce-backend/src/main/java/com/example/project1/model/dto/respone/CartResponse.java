package com.example.project1.model.dto.respone;

import lombok.Data;

import java.util.Set;

@Data
public class CartResponse {
    private Long id;

    private Long userId;

    private Set<CartItemResponse> cartItems;
}
