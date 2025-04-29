package com.example.project1.model.dto.product;

import com.example.project1.model.enity.product.Attribute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValueDTO {
    private Long id;

    private Long attributeId;

    private AttributeDto attribute;

    private String name;
}
