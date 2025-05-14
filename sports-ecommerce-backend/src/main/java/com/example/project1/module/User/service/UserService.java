package com.example.project1.module.User.service;

import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.enity.User.UserVerification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDto create(UserCreateRequest request);

    UserDto update( UserCreateRequest request, Long id);

    void delete(Long id);
    List<UserDto> findAll();
    UserDto register(UserVerification request);

    UserDto getMyInfo();

    Object createStaff(UserCreateRequest request);

    Object getStaff();

    Object getUser();

    Object updateActive(Long id);

    void deleteUser(Long userId);
   Object getMyInf();

   Object changepassword(String passwrodOld, String passwordNew);

   Object updateImage(MultipartFile image, Long id);
}
