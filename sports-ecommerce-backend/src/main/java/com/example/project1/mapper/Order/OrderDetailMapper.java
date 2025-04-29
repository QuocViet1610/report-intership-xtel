package com.example.project1.mapper.Order;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.Order.OrderDetailDto;
import com.example.project1.model.dto.Order.OrderDto;
import com.example.project1.model.dto.request.Order.AddressCreateRequest;
import com.example.project1.model.dto.request.Order.OrderDetailCreateRequest;
import com.example.project1.model.dto.request.Order.OrderRequest;
import com.example.project1.model.dto.respone.CartItemResponse;
import com.example.project1.model.dto.respone.OrderDetailResponse;
import com.example.project1.model.dto.respone.OrderResponse;
import com.example.project1.model.enity.cart.CartItem;
import com.example.project1.model.enity.order.Address;
import com.example.project1.model.enity.order.Order;
import com.example.project1.model.enity.order.OrderDetail;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class OrderDetailMapper implements EntityMapper<OrderDetailDto, OrderDetail> {

//    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
//    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
//    @Mapping(target = "orderDate", expression = Constants.EXPRESSION.CURRENT_DATE)
//    @Mapping(target = "orderDetails",ignore = true)
    public abstract OrderDetail toCreate(OrderDetailCreateRequest createRequest);

    @Mapping(target = "id",ignore = true)
    public abstract void partialUpdate(@MappingTarget Address address, AddressCreateRequest request);

    @Mapping(target = "productView",ignore = true)
    public abstract OrderDetailResponse toResponse(OrderDetail cart);
}
