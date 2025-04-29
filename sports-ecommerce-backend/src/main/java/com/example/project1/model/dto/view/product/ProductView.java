package com.example.project1.model.dto.view.product;

import com.example.project1.model.enity.product.ProductImage;
import com.example.project1.model.enity.product.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "product_view")
@Getter
@Setter
public class ProductView {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private Double productPrice;

    @Column(name = "product_cost_price")
    private Double productCostPrice;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_is_active")
    private Boolean productIsActive;

    @Column(name = "product_total_sold")
    private Long productTotalSold;

    @Column(name = "product_total_rating")
    private Double productTotalRating;

    @Column(name = "product_stock")
    private Long productStock;

    @Column(name = "product_discount_price")
    private Double productDiscountPrice;

    @Column(name = "product_weight")
    private Double productWeight;

    @Column(name = "product_created_at")
    private OffsetDateTime createdAt;

    @Column(name = "product_updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "product_created_by")
    private String createdBy;

    @Column(name = "product_updated_by")
    private String updatedBy;

    @Column(name = "gender_id")
    private Long genderId;

    @Column(name = "gender_name")
    private String genderName;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_image")
    private String categoryImage;

    @Column(name = "full_parent_id")
    private String fullParentId;

    @JsonIgnore
    @OneToMany(mappedBy = "productView", fetch = FetchType.LAZY)
    private Set<ProductVariant> productVariants;
//    @JsonIgnore
//    @OneToMany(mappedBy = "productView", fetch = FetchType.LAZY)
//    private Set<ProductAttribute> productAttributes;
//    @JsonIgnore
//    @OneToMany(mappedBy = "productView", fetch = FetchType.LAZY)
//    private Set<ProductAttributeValue> productAttributeValues;
    @JsonIgnore
    @OneToMany(mappedBy = "productView", fetch = FetchType.LAZY)
    private Set<ProductImage> productImages;

    @JsonIgnore
    @OneToMany(mappedBy = "productView", fetch = FetchType.LAZY)
    private Set<ProductAttributeValueView> productAttributeValueView;
}
