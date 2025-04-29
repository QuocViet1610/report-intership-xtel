package com.example.project1.mapper.product;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.product.AttributeValueDTO;
import com.example.project1.model.enity.product.AttributeValue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AttributeValueMapper implements EntityMapper<AttributeValueDTO, AttributeValue> {
}
