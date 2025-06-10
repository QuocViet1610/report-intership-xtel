package com.example.project1.module.User.service.serviceImpl;

import com.example.project1.mapper.User.RoleMapper;
import com.example.project1.model.dto.User.RoleDto;
import com.example.project1.model.dto.request.RoleRequest;

import com.example.project1.module.User.repository.PermissionRepository;
import com.example.project1.module.User.repository.RoleRepository;
import com.example.project1.module.User.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImp implements RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    @Override
    public RoleDto create(RoleRequest request) {
//        Role role = roleMapper.toEntity(request);
//
//        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
//        role.setPermissions(new HashSet<>(permissions));
//        return roleMapper.toDto(roleRepository.save(role));
        return null;
    }

    @Override
    public List<RoleDto> getAll() {
//        return roleMapper.toDto(roleRepository.findAll());
        return null;
    }


    @Override
    public void delete(String role) {
//          roleRepository.deleteById(role);

    }

}
