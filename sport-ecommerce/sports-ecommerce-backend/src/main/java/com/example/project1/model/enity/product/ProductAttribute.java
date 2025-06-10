package com.example.project1.model.enity.product;

import com.example.project1.model.BaseEntity;
import com.example.project1.model.dto.view.product.ProductView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Table(name = "product_attribute")
@Entity
@Setter
@Getter
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false, insertable = false, updatable = false)
    private Attribute attribute;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false )
//    private ProductView productView;

}
