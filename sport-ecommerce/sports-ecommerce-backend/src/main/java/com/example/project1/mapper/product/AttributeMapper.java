package com.example.project1.mapper.product;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.product.AttributeDto;
import com.example.project1.model.dto.request.product.AttributeCreateRequest;
import com.example.project1.model.enity.product.Attribute;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AttributeMapper implements EntityMapper<AttributeDto, Attribute> {

    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "createdBy", expression = Constants.EXPRESSION.CURRENT_USER)
    public abstract Attribute toCreate(AttributeCreateRequest createRequest);

    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "displayOrder",ignore = true)
    public abstract void partialUpdate(@MappingTarget Attribute attribute, AttributeCreateRequest request);
}
