package com.example.project1.module.controller.account;
import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.dto.request.product.CategoryBaseRequest;
import com.example.project1.module.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @GetMapping()
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseResult<List<UserDto>> getAllUser()
    {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();

        log.info("UserName: {}",authentication.getName() );
        log.info("Roles: {}",authentication.getAuthorities() );

        return ResponseResult.ofSuccess(userService.findAll());
    }

    @PostMapping("/register")
    public ResponseResult<UserDto> createUser(@RequestBody @Valid UserCreateRequest request){
        return ResponseResult.ofSuccess(userService.create(request));
    }

    @GetMapping("/test")
    public ResponseResult<List<UserDto>> test()
    {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        return ResponseResult.ofSuccess(userService.findAll());
    }
    @GetMapping("/my-infor")
    public ResponseResult<UserDto> getMyProfile()
    {
        return ResponseResult.ofSuccess(userService.getMyInfo());
    }

    @PostMapping("/createStaff")
    public ResponseResult<Object> createStaff(@RequestBody @Valid UserCreateRequest request)
    {
        return ResponseResult.ofSuccess(userService.createStaff(request));
    }

    @GetMapping("/get-staff")
    public ResponseResult<Object> geStaff()
    {
        return ResponseResult.ofSuccess(userService.getStaff());
    }

    @Operation(summary = "Lấy danh sách user", description = "Trả về danh sách tất cả user")
    @GetMapping("/get-user")
    public ResponseResult<Object> getUser()
    {
        return ResponseResult.ofSuccess(userService.getUser());
    }

    @PutMapping("/update-active/{id}")
    public ResponseResult<Object> lock(@PathVariable Long id) {
        return ResponseResult.ofSuccess(userService.updateActive(id));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseResult.ofSuccess();
    }
    @GetMapping("/get-inf")
    public ResponseResult<Object> getMyInf() {
        return ResponseResult.ofSuccess(userService.getMyInf());
    }

    @PutMapping("/change-password-update")
    public ResponseResult<Object> changePassword(
                                                 @RequestParam String passwordOld,
                                                 @RequestParam String passwordNew) {
        return ResponseResult.ofSuccess( userService.changepassword(passwordOld, passwordNew));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<Object> updateCategory(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        return ResponseResult.ofSuccess(userService.updateImage(image, id));
    }

    @GetMapping("/get-detail/{id}")
    public ResponseResult<Object> getDetail(@PathVariable Long id) {
        return ResponseResult.ofSuccess(userService.getUserDetail(id));
    }

    @PutMapping("/update/{id}")
    public ResponseResult<Object> updateUser(@PathVariable Long id, @RequestBody @Valid UserCreateRequest userCreateRequest) {
        return ResponseResult.ofSuccess(userService.update(userCreateRequest , id));
    }
}
