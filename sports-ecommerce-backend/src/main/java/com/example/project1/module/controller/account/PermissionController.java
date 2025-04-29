package com.example.project1.module.controller.account;

import com.example.project1.model.dto.User.PermissionDto;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.request.PermissionRequest;
import com.example.project1.module.User.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ResponseResult<PermissionDto> create(@RequestBody PermissionRequest request) {
        return ResponseResult.ofSuccess(permissionService.create(request));
    }

    @GetMapping
    ResponseResult<List<PermissionDto>> getAll() {
        return ResponseResult.ofSuccess(permissionService.getAll());
    }

//    @DeleteMapping("/{permission}")
//    ResponseResult<String> delete(@PathVariable String permission) {
//        permissionService.delete(permission);
//        return ResponseResult.ofSuccess();
//    }
}
