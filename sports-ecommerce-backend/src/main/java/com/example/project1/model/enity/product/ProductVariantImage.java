package com.example.project1.model.enity.product;

import com.example.project1.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table(name = "product_variants_image")
@Entity
@Setter
@Getter
public class ProductVariantImage  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "product_variant_id", nullable = false)
    private Long productVariantId;

    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false, insertable = false, updatable = false)
    private ProductVariant productVariant;

    private String image;

    @Column(name = "is_primary")
    private Boolean isPrimary;

}
