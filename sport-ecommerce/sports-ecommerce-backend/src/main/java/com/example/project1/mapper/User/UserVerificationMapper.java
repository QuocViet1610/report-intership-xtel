package com.example.project1.mapper.User;

import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.request.RegisterRequest;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.User.UserVerification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UserVerificationMapper implements EntityMapper<RegisterRequest, UserVerification> {

    @Mapping(target = "id", ignore = true)
    public abstract void  partialUpdate(@MappingTarget UserVerification user, RegisterRequest request);

}
