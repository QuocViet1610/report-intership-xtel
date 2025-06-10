package com.example.project1.model.enity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Table(name = "product_variant_attribute")
@Entity
@Setter
@Getter
public class ProductVariantAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "attribute_value_id", nullable = false)
    private Long attributeValueId;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "attribute_value_id", nullable = false, insertable = false, updatable = false)
    private AttributeValue attributeValue;



}
