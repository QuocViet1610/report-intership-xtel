package com.example.project1.mapper.product;

import com.example.project1.model.dto.request.product.ProductVariantCreateRequest;
import com.example.project1.model.enity.product.ProductVariant;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductVariantMapper  {
    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "createdBy", expression = Constants.EXPRESSION.CURRENT_USER)
    public abstract ProductVariant toCreate(ProductVariantCreateRequest createRequest);

    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "createdBy", expression = Constants.EXPRESSION.CURRENT_USER)
    public abstract List<ProductVariant> toCreate(List<ProductVariantCreateRequest> createRequest);

    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "id",ignore = true)
    public abstract void partialUpdate(@MappingTarget ProductVariant productVariant, ProductVariantCreateRequest request);
}
