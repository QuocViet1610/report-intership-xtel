package com.example.project1.mapper.Order;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.Order.AddressDto;
import com.example.project1.model.dto.request.Order.AddressCreateRequest;
import com.example.project1.model.enity.order.Address;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AddressMapper implements EntityMapper<AddressDto, Address> {
    @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
    public abstract Address toCreate(AddressCreateRequest createRequest);

    @Mapping(target = "id",ignore = true)
    public abstract void partialUpdate(@MappingTarget Address address, AddressCreateRequest request);
}
