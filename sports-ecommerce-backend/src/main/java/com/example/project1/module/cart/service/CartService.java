package com.example.project1.module.cart.service;

import com.example.project1.model.dto.cart.CartDto;
import com.example.project1.model.dto.cart.CartItemCreateRequest;
import com.example.project1.model.dto.cart.CartItemDto;
import com.example.project1.model.dto.respone.CartResponse;


public interface CartService {
    CartItemDto addProductToCart(CartItemCreateRequest request);

    CartItemDto updateQuantity(Long cartItemId, Long quantity);

    void removeProductFromCart(Long cartItemId);

    CartResponse getCartByUserId();
}
