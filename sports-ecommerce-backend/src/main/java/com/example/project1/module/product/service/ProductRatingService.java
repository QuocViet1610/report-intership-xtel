package com.example.project1.module.product.service;

import com.example.project1.model.dto.product.ProductDto;
import com.example.project1.model.dto.product.ProductRatingDto;
import com.example.project1.model.dto.request.product.ProductRatingCreateRequest;
import com.example.project1.model.dto.request.product.ProductRatingSearchRequest;
import com.example.project1.model.enity.product.ProductRating;
import com.example.project1.module.PageableCustom;

public interface ProductRatingService {

    void delete(Long id);
    Object search(ProductRatingSearchRequest searchRequest, PageableCustom pageable);
    ProductRatingDto create(ProductRatingCreateRequest request);


}
