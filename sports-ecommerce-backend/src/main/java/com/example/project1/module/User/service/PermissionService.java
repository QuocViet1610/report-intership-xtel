package com.example.project1.module.User.service;

import com.example.project1.model.dto.User.PermissionDto;
import com.example.project1.model.dto.request.PermissionRequest;

import java.util.List;

public interface PermissionService {

    PermissionDto create(PermissionRequest request);

    List<PermissionDto> getAll();

    void delete(String permission);
}
