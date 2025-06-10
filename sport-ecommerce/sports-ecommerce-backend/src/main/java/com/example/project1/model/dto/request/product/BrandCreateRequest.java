package com.example.project1.model.dto.request.product;

import lombok.Data;

@Data
public class BrandCreateRequest {
    private Long id;

    private String name;

    private String description;

    private String logo;
}
