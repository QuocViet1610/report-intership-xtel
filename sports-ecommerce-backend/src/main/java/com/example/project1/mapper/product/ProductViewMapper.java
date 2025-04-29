package com.example.project1.mapper.product;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.request.product.ProductVariantCreateRequest;
import com.example.project1.model.dto.view.product.ProductView;
import com.example.project1.model.dto.view.product.ProductViewDto;
import com.example.project1.model.enity.product.ProductVariant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductViewMapper implements EntityMapper<ProductViewDto, ProductView> {



}
