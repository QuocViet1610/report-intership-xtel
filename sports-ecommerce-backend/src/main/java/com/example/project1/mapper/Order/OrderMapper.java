package com.example.project1.mapper.Order;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.Order.AddressDto;
import com.example.project1.model.dto.Order.OrderDto;
import com.example.project1.model.dto.request.Order.AddressCreateRequest;
import com.example.project1.model.dto.request.Order.OrderRequest;
import com.example.project1.model.dto.respone.CartResponse;
import com.example.project1.model.dto.respone.OrderResponse;
import com.example.project1.model.enity.cart.Cart;
import com.example.project1.model.enity.order.Address;
import com.example.project1.model.enity.order.Order;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class OrderMapper implements EntityMapper<OrderDto, Order> {

    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "orderDetails",ignore = true)
    public abstract Order toCreate(OrderRequest createRequest);

    @Mapping(target = "id",ignore = true)
    public abstract void partialUpdate(@MappingTarget Address address, AddressCreateRequest request);

    @Mapping(target = "orderDetails",ignore = true)
    public abstract OrderResponse toResponse(Order order);

}
