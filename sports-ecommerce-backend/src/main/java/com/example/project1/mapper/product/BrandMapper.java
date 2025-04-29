package com.example.project1.mapper.product;
import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.product.BrandDto;
import com.example.project1.model.dto.request.product.BrandCreateRequest;
import com.example.project1.model.enity.product.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class BrandMapper implements EntityMapper<BrandDto, Brand> {


     public abstract Brand toCreate(BrandCreateRequest request);


     @Mapping(target = "id", ignore = true)
     public abstract void  partialUpdate(@MappingTarget Brand brand, BrandCreateRequest request);
}
