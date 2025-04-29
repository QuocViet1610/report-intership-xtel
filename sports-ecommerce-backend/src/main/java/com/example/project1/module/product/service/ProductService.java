package com.example.project1.module.product.service;


import com.example.project1.model.dto.product.ProductDto;

import com.example.project1.model.dto.request.product.ProductBaseRequest;
import com.example.project1.model.dto.request.product.ProductSearchRequest;
import com.example.project1.model.dto.view.product.ProductView;
import com.example.project1.model.dto.view.product.ProductViewDto;
import com.example.project1.module.PageableCustom;

import java.util.List;

public interface ProductService {

    void delete(Long id);
    Object search(ProductSearchRequest searchRequest, PageableCustom pageable);
    ProductDto create(ProductBaseRequest request);
    public ProductDto update(ProductBaseRequest request, Long id) ;

    List<ProductViewDto> getAll();

    ProductViewDto getDetail(Long id);
}
