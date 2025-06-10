package com.example.project1.mapper.cart;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.cart.CartDto;
import com.example.project1.model.dto.request.product.AttributeCreateRequest;
import com.example.project1.model.dto.respone.CartResponse;
import com.example.project1.model.enity.cart.Cart;
import com.example.project1.model.enity.product.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CartMapper implements EntityMapper<CartDto, Cart> {

    @Mapping(target = "cartItems",ignore = true)
    public abstract CartResponse toResponse(Cart cart);

}
