package com.example.project1.module.controller.account;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.module.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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
}
