package com.example.project1.model.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductImageDto {
    private Long id;

    private Long productId;

    private String imageUrl;

    private int isPrimary ;

}
