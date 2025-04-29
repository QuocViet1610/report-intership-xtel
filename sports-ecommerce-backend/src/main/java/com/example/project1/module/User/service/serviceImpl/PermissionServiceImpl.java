package com.example.project1.module.User.service.serviceImpl;

import com.example.project1.mapper.User.PermissionMapper;
import com.example.project1.model.dto.User.PermissionDto;
import com.example.project1.model.dto.request.PermissionRequest;
import com.example.project1.module.User.repository.PermissionRepository;
import com.example.project1.module.User.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionDto create(PermissionRequest request) {
//        Permission permission = permissionMapper.toEntity(request);
//        permission = permissionRepository.save(permission);
//        return permissionMapper.toDto(permission);
        return null;
    }

    @Override
    public List<PermissionDto> getAll() {
//        return permissionMapper.toDtos(permissionRepository.findAll());
        return null;
    }

    @Override
    public void delete(String permission) {
//        permissionRepository.deleteById(permission);
    }
}
