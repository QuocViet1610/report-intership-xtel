package com.example.project1.mapper.product;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.product.CategoryCreateRequest;
import com.example.project1.model.enity.product.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper implements EntityMapper<CategoryDto, Category> {


     public abstract Category toCreate(CategoryCreateRequest request);


     public abstract Category toEntity(CategoryCreateRequest request);

     @Mapping(target = "id", ignore = true)
     public abstract void  partialUpdate(@MappingTarget Category category, CategoryCreateRequest request);
}
