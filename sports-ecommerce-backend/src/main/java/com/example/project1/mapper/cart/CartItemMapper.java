package com.example.project1.mapper.cart;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.cart.CartDto;
import com.example.project1.model.dto.cart.CartItemDto;
import com.example.project1.model.dto.respone.CartItemResponse;
import com.example.project1.model.dto.respone.CartResponse;
import com.example.project1.model.enity.cart.Cart;
import com.example.project1.model.enity.cart.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CartItemMapper implements EntityMapper<CartItemDto, CartItem> {
    @Mapping(target = "productView",ignore = true)
    public abstract CartItemResponse toResponse(CartItem cart);
}
