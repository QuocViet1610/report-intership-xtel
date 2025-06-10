package com.example.project1.module.product.service;

import com.example.project1.model.dto.product.BrandDto;
import com.example.project1.model.dto.request.product.BrandBaseRequest;
import com.example.project1.model.dto.request.product.BrandSearchRequest;
import com.example.project1.model.enity.product.Brand;
import com.example.project1.module.PageableCustom;

import java.util.List;

public interface BrandService {
    void delete(Long id);

    Object search(BrandSearchRequest searchRequest, PageableCustom pageable);
    BrandDto create(BrandBaseRequest request);
    public BrandDto update(BrandBaseRequest request, Long id) ;

    List<BrandDto> getAll();
}
