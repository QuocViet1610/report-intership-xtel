package com.example.project1.model.enity.product;

import com.example.project1.model.BaseEntity;
import com.example.project1.model.dto.view.product.ProductView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Table(name = "product_variants")
@Entity
@Setter
@Getter
public class ProductVariant extends BaseEntity {
    @Column(nullable = false)
    private String code;

    private String name;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    private String image ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false )
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false )
    private ProductView productView;
    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice;

    @Column(nullable = false)
    private Long quantity;

    @JsonIgnore
    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductVariantAttribute> variantAttributes;
}
