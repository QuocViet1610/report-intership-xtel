package com.example.project1.model.dto.product;

import com.example.project1.model.enity.product.AttributeValue;
import lombok.Data;

@Data
public class ProductVariantAttributeDto {

    private Long id;

    private Long attributeValueId;

//    private ProductVariant productVariant;


    private AttributeValueDTO attributeValue;

}
