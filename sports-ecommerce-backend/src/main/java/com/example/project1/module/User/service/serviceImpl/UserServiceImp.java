package com.example.project1.module.User.service.serviceImpl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.User.UserMapper;
import com.example.project1.model.Enum.RoleEnum;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.User.UserRole;
import com.example.project1.model.enity.User.UserVerification;
import com.example.project1.module.User.repository.RoleRepository;
import com.example.project1.module.User.repository.UserRepository;
import com.example.project1.module.User.repository.UserRoleRepository;
import com.example.project1.module.User.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;
    EmailService emailService;

    private void validateLogic(UserCreateRequest request, boolean isCreate){
          if (userRepository.findByEmail(request.getEmail()).isPresent()){
              throw new ValidateException(Translator.toMessage("error.user.user_email.is_exit"));
          }
        if (userRepository.findByPhone(request.getPhone()).isPresent()){
            throw new ValidateException(Translator.toMessage("error.user.user_name.is_exit"));
        }
    }


    @Override
    public UserDto create(UserCreateRequest request) {
        this.validateLogic(request,true);
//        List<Role> roles = roleRepository.findAllById(request.getRoles());

        User userRequest = userMapper.toEntity(request);
//        userRequest.setPassword(passwordEncoder.encode(request.getPassword()));
        User user= userRepository.save(userRequest);
        UserRole userRole = new UserRole();
        userRole.setRoleId(RoleEnum.USER.getId());
        userRole.setUserId(user.getId());
        userRoleRepository.save(userRole);

        return userMapper.toDto(userRequest);
    }



    @Override
    public UserDto update(UserCreateRequest request,Long id) {
        this.validateLogic(request,true);
        userRepository.findById(id).map(user -> {
            this.validateLogic(request,false);

            return null;
        }).orElseThrow(() -> new ValidateException("error.node.id.not_exist"));
        return null;
    }

    @Override

    public List<UserDto> findAll() {
        return userMapper.toDto(userRepository.findAll());
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public UserDto register(UserVerification request) {
        User userRequest = userMapper.toCreate(request);
//        userRequest.setPassword(passwordEncoder.encode(request.getPassword()));
        User user= userRepository.save(userRequest);
        UserRole userRole = new UserRole();
        userRole.setRoleId(RoleEnum.USER.getId());
        userRole.setUserId(user.getId());
        userRoleRepository.save(userRole);

        return userMapper.toDto(userRequest);
    }

    @Override
    public UserDto getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByEmail(name).orElseThrow(() -> new ValidateException("error.user.user_name.not_exist"));

        return userMapper.toDto(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }



}
