package com.example.project1.module.Order.service;

import com.example.project1.model.dto.Order.AddressDto;
import com.example.project1.model.dto.cart.CartItemCreateRequest;
import com.example.project1.model.dto.cart.CartItemDto;
import com.example.project1.model.dto.request.Order.AddressCreateRequest;
import com.example.project1.model.dto.respone.CartResponse;

import java.util.List;


public interface AddressService {
    AddressDto create(AddressCreateRequest request);

    AddressDto update(Long id,AddressCreateRequest request);

    void delete(Long id);

    List<AddressDto> getAll();

    AddressDto getDetail(Long id);
}
