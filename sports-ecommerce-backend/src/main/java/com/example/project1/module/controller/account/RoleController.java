package com.example.project1.module.controller.account;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.User.RoleDto;
import com.example.project1.model.dto.request.RoleRequest;
import com.example.project1.module.User.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping

    ResponseResult<RoleDto> create(@RequestBody RoleRequest request) {
        return ResponseResult.ofSuccess(roleService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SELECT_ROLE')")
    ResponseResult<List<RoleDto>> getAll() {
        return ResponseResult.ofSuccess(roleService.getAll());
    }

//    @DeleteMapping("/{role}")
//    ResponseResult<String> delete(@PathVariable String role) {
//        roleService.delete(role);
//        return ResponseResult.ofSuccess();
//    }
}
