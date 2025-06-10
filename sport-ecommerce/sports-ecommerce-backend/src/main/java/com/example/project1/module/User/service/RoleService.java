package com.example.project1.module.User.service;

import com.example.project1.model.dto.User.RoleDto;
import com.example.project1.model.dto.request.RoleRequest;

import java.util.List;

public interface RoleService {

    RoleDto create(RoleRequest request);

    List<RoleDto> getAll();

    void delete(String role);

}
