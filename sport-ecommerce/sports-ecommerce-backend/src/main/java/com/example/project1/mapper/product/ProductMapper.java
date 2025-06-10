package com.example.project1.mapper.product;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.product.ProductDto;
import com.example.project1.model.dto.request.product.ProductCreateRequest;
import com.example.project1.model.enity.product.Product;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public abstract class ProductMapper implements EntityMapper<ProductDto, Product> {
    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "createdBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "isActive",constant = "1")
    @Mapping(target = "totalRating",expression = "java(new java.math.BigDecimal(0))")
    @Mapping(target = "totalSold",constant = "0L")
    @Mapping(target = "discountPrice", expression = "java(new java.math.BigDecimal(0))")
    public abstract Product toCreate(ProductCreateRequest createRequest);

    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "code",ignore = true)
    public abstract void partialUpdate(@MappingTarget Product product, ProductCreateRequest request);
}
