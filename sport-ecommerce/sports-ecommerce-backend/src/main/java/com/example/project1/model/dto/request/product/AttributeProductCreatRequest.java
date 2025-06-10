package com.example.project1.model.dto.request.product;

import lombok.Data;

import java.util.List;

@Data
public class AttributeProductCreatRequest {
    private Long attributeId;
    private List<Long> attributeValueIds;
}
