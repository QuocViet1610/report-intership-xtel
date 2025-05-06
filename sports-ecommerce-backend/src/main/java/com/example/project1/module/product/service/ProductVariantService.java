package com.example.project1.module.product.service;

import com.example.project1.model.dto.product.ProductVariantDto;
import com.example.project1.model.dto.request.product.ProductVariantCreateRequest;
import com.example.project1.model.dto.request.product.ProductVariantUpdate;
import com.example.project1.model.enity.product.ProductVariant;

public interface ProductVariantService {

    void delete(Long id);

    ProductVariant update(Long id, ProductVariantUpdate productVariantCreateRequest);

    ProductVariant getDetail(Long id);
}
