package com.example.project1.mapper.product;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.product.ProductRatingDto;
import com.example.project1.model.dto.request.product.ProductRatingCreateRequest;
import com.example.project1.model.enity.product.ProductRating;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ProductRatingMapper implements EntityMapper<ProductRatingDto, ProductRating> {
    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedBy", expression = Constants.EXPRESSION.CURRENT_USER)
    @Mapping(target = "createdBy", expression = Constants.EXPRESSION.CURRENT_USER)
    public abstract ProductRating toCreate(ProductRatingCreateRequest createRequest);
}
