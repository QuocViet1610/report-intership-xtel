package com.example.project1.model.dto.view.product.PrimaryKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ProductAttributeValueViewId implements Serializable {
//    @Column(name = "product_id")
    private Long productId;
//    @Column(name = "attribute_id")
    private Long attributeId;
    public ProductAttributeValueViewId() {}

    public ProductAttributeValueViewId(Long productId, Long attributeId) {
        this.productId = productId;
        this.attributeId = attributeId;
    }

}
