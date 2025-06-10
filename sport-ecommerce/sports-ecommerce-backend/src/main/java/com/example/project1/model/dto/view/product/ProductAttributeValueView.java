package com.example.project1.model.dto.view.product;

import com.example.project1.model.dto.product.AttributeValueDTO;
import com.example.project1.model.dto.view.product.PrimaryKey.ProductAttributeValueViewId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product_attribute_value_view")
@Getter
@Setter
@IdClass(ProductAttributeValueViewId.class)
public class ProductAttributeValueView {
//    @Id
//    @Column(name = "product_id")
//    private Long productId;
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Id
    @Column(name = "attribute_id")
    private Long attributeId;

    @Column(name = "product_name")
    private String productName;

//    @Column(name = "attribute_id")
//    private Long attributeId;

    @Column(name = "attribute_name")
    private String attributeName;

    @Column(name = "attribute_values")
    @JsonIgnore
    private String attributeValuesJson;

    public List<AttributeValueDTO> getAttributeValues() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(attributeValuesJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, AttributeValueDTO.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false, insertable = false )
    @JsonIgnore
    private ProductView productView;
}
