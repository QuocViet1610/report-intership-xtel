package com.example.project1.module.Order.service;

import com.example.project1.model.dto.Order.OrderDto;
import com.example.project1.model.dto.request.Order.OrderRequest;
import com.example.project1.model.dto.request.Order.OrderSearchRequest;
import com.example.project1.model.dto.request.product.ProductSearchRequest;
import com.example.project1.model.dto.respone.OrderResponse;
import com.example.project1.module.PageableCustom;

import java.util.List;

public interface OrderService {
    OrderDto create(OrderRequest orderCreateRequest);

    OrderResponse getOrderDetail(Long id);

    List<OrderDto> getAllOrder();
    Object search(OrderSearchRequest searchRequest, PageableCustom pageable);

    OrderDto UpdateProcess(Long id);

    OrderDto CancelOrder(Long id);

    Object Dashboard();
}
