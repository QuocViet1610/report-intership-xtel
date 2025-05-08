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
import com.example.project1.module.Order.repository.OrderRepository;
import com.example.project1.module.User.repository.RoleRepository;
import com.example.project1.module.User.repository.UserRepository;
import com.example.project1.module.User.repository.UserRoleRepository;
import com.example.project1.module.User.service.UserService;
import com.example.project1.utils.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    OrderRepository orderRepository;
    TokenUtil tokenUtil;


    private void validateLogic(UserCreateRequest request, boolean isCreate){
        if (isCreate){
            if (userRepository.findByEmail(request.getEmail()).isPresent()){
                throw new ValidateException(Translator.toMessage("error.user.user_email.is_exit"));
            }
            if (userRepository.findByPhone(request.getPhone()).isPresent()){
                throw new ValidateException(Translator.toMessage("Số điện thoại đã tồn tại "));
            }
        }else {
            if (userRepository.findByEmailAndIdNot(request.getEmail(),request.getId() ).isPresent()){
                throw new ValidateException(Translator.toMessage("error.user.user_email.is_exit"));
            }
            if (userRepository.findByPhoneAndIdNot(request.getPhone(), request.getId()).isPresent()){
                throw new ValidateException(Translator.toMessage("Số điện thoại đã tồn tại "));
            }
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
        Long idUser = tokenUtil.getCurrentUserId();

        return userRepository.findById(idUser).map(user -> {
            boolean authenticated = request.getPassword().equals(user.getPassword());
            if (!authenticated){
                request.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            request.setId(user.getId());
            this.validateLogic(request,false);
            userMapper.partialUpdate(user, request);
            return userMapper.toDto(user);
        }).orElseThrow(() -> new ValidateException("Tài khoản không tồn tại"));

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

    @Override
    public Object createStaff(UserCreateRequest request) {
        this.validateLogic(request,true);
//        List<Role> roles = roleRepository.findAllById(request.getRoles());

        User userRequest = userMapper.toEntity(request);
        userRequest.setPassword(passwordEncoder.encode(request.getPassword()));
        User user= userRepository.save(userRequest);
        UserRole userRole = new UserRole();
        userRole.setRoleId(RoleEnum.ADMIN.getId());
        userRole.setUserId(user.getId());
        userRoleRepository.save(userRole);

        return userMapper.toDto(userRequest);
    }

    @Override
    public Object getStaff() {
        return userRepository.findUsersByRoleId();
    }

    @Override
    public Object getUser() {
        return userRepository.findUsersByRoleIdUser();
    }

    @Override
    public Object updateActive(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidateException("Người dùng không tồn tại "));

        user.setIsActive(user.getIsActive() == 1 ? 0 : 1);
        return  userRepository.save(user);
    }

    public void deleteUser(Long userId) {

        if (orderRepository.findAllByUserId(userId).size() > 0 ){
            User user = userRepository.findById(userId).orElseThrow(() -> new ValidateException("Người dùng không tồn tại "));
            user.setIsActive(0);
            userRepository.save(user);
        }else {
            userRepository.deleteById(userId);
        }

    }

    public Object getMyInf() {
        Long id = tokenUtil.getCurrentUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new ValidateException("Người dùng không tồn tại "));
        return user;
    }


    @Override
    public Object changepassword(String passwrodOld, String passwordNew) {
        Long id = tokenUtil.getCurrentUserId();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ValidateException("error.user.user_name.not_exist"));

        boolean authenticated = passwordEncoder.matches(passwrodOld, user.getPassword());
        if (authenticated){
            user.setPassword(passwordEncoder.encode(passwordNew));
        }else {
            throw new ValidateException(Translator.toMessage("Mật khẩu hiện tại không đúng "));
        }

        return userRepository.save(user);
    }
}
