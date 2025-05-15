package com.example.project1.model.dto.request.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSearchRequest {
    private String searchText;
    private Long brandId;
    private Long categoryId;
    private Long genderId;
    private String fullParentId;
    private Long categorySearch;
    private String[] attributeSearch;

    private Double minPrice;
    private Double maxPrice;
    private Integer productIsActive;
}

