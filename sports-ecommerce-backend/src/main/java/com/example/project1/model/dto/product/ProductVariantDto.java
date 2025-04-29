package com.example.project1.model.dto.product;

import com.example.project1.model.dto.BaseDto;
import com.example.project1.model.enity.product.ProductVariantAttribute;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class ProductVariantDto {
    private Long id;

    private String code;

    private String name;

    private String image;

    private Long productId;

    private BigDecimal price;

    private BigDecimal costPrice;

    private Long quantity;

    private Set<ProductVariantAttributeDto> variantAttributes;
}
