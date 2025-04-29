package com.example.project1.model.dto.view.product;

import com.example.project1.model.dto.product.ProductVariantDto;
import com.example.project1.model.enity.product.ProductImage;
import com.example.project1.model.enity.product.ProductVariant;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class ProductViewDto {

        private Long productId;
        private String productCode;
        private String productName;
        private Double productPrice;
        private Double productCostPrice;
        private String productDescription;
        private Boolean productIsActive;
        private Long productTotalSold;
        private Double productTotalRating;
        private Long productStock;
        private Double productDiscountPrice;
        private Double productWeight;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private String createdBy;
        private String updatedBy;
        private Long genderId;
        private String genderName;
        private Long brandId;
        private String brandName;
        private Long categoryId;
        private String categoryName;
        private String categoryImage;
        private Set<ProductVariantDto> productVariants;
        private String fullParentId;
//        private Set<ProductAttribute> productAttributes;
//        private Set<ProductAttributeValue> productAttributeValues;
        private Set<ProductImage> productImages;
        @JsonProperty("productAttributeValue")
        private Set<ProductAttributeValueView> productAttributeValueView;
}
