package com.example.project1.model.dto.request.product;

import lombok.Data;

@Data
public class ProductSearchRequest {
    private String searchText;
    private Long brandId;
    private Long categoryId;
    private Long genderId;
    private String fullParentId;
    private Long categorySearch;
}

