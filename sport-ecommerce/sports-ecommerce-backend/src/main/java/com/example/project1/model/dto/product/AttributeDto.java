package com.example.project1.model.dto.product;

import com.example.project1.model.dto.BaseDto;
import com.example.project1.model.enity.product.AttributeValue;
import lombok.Data;

import java.util.Set;

@Data
public class AttributeDto extends BaseDto {

    private String name;

    private String description;

    private Integer displayOrder;

    private Set<AttributeValue> values;

}
