package com.example.project1.mapper.User;
import com.example.project1.mapper.EntityMapper;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.User.UserVerification;
import com.example.project1.utils.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements EntityMapper<UserDto, User> {

     @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
     @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
     @Mapping(target = "isActive",constant = "1")
     public abstract User toCreate(UserVerification userCreateRequest);

     @Mapping(target = "createdAt", expression = Constants.EXPRESSION.CURRENT_DATE)
     @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
     @Mapping(target = "isActive",constant = "1")
     public abstract User toEntity(UserCreateRequest userCreateRequest);

     public abstract List<UserDto> toDto(List<User> user);

     @Mapping(target = "updatedAt", expression = Constants.EXPRESSION.CURRENT_DATE)
     @Mapping(target = "id", ignore = true)
     public abstract void  partialUpdate(@MappingTarget User user, UserCreateRequest request);
}
