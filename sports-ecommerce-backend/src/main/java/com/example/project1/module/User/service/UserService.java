package com.example.project1.module.User.service;

import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.enity.User.UserVerification;

import java.util.List;

public interface UserService {
    UserDto create(UserCreateRequest request);

    UserDto update( UserCreateRequest request, Long id);

    void delete(Long id);
    List<UserDto> findAll();
    UserDto register(UserVerification request);

    UserDto getMyInfo();
}
