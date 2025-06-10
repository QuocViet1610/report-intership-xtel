package com.example.project1.module.User.service.serviceImpl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.User.UserMapper;
import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.Enum.RoleEnum;
import com.example.project1.model.config.MinioConfig;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.dto.request.product.CategoryBaseRequest;
import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.User.UserRole;
import com.example.project1.model.enity.User.UserVerification;
import com.example.project1.module.Order.repository.OrderRepository;
import com.example.project1.module.User.repository.RoleRepository;
import com.example.project1.module.User.repository.UserRepository;
import com.example.project1.module.User.repository.UserRoleRepository;
import com.example.project1.module.User.repository.UserViewRepository;
import com.example.project1.module.User.service.UserService;
import com.example.project1.utils.DataUtils;
import com.example.project1.utils.MinioUtils;
import com.example.project1.utils.TokenUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final TokenUtil tokenUtil;
    private final MinioConfig minioConfig;
    private String bucketName ;
    private String folderLocal;
    private String keyName ;
    private final UserViewRepository userViewRepository;

    @PostConstruct
    void started() {
        bucketName = minioConfig.getMinioBucketName();
        keyName = minioConfig.getMinioUserKeyName();
        folderLocal = minioConfig.getMinioUserFolder();
    }

    private void validateLogic(UserCreateRequest request, boolean isCreate){
        if (isCreate){
            if (userRepository.findByEmail(request.getEmail()).isPresent()){
                throw new ValidateException(Translator.toMessage("Email đã tồn tại"));
            }
            if (userRepository.findByPhone(request.getPhone()).isPresent()){
                throw new ValidateException(Translator.toMessage("Số điện thoại đã tồn tại "));
            }
        }else {
            if (userRepository.findByEmailAndIdNot(request.getEmail(),request.getId() ).isPresent()){
                throw new ValidateException(Translator.toMessage("Email đã tồn tại"));
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
//        Long idUser = tokenUtil.getCurrentUserId();

        return userRepository.findById(id).map(user -> {
//            boolean authenticated = request.getPassword().equals(user.getPassword());
//            if (!authenticated){
//                request.setPassword(passwordEncoder.encode(request.getPassword()));
//            }
            request.setId(user.getId());
            this.validateLogic(request,false);
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setFullName(request.getFullName());
            userRepository.save(user);
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
        return userViewRepository.findAll();
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

    public Object updateImage(MultipartFile image, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ValidateException("Người dùng không tồn tại "));
        if(!DataUtils.isNullOrEmpty(image)){
            user.setAvatar(MinioUtils.uploadToMinioAndGetUrl(image, folderLocal, bucketName, keyName));
        }
        if (!DataUtils.isNullOrEmpty(image)){
            user.setAvatar(MinioUtils.uploadToMinioAndGetUrl(image, folderLocal, bucketName, keyName));
            MinioUtils.deleteFileMinio(bucketName, user.getAvatar());
        }else {
            user.setAvatar(user.getAvatar());
        }
        return user;
    }

    public Object getUserDetail(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ValidateException("Người dùng không tồn tại "));
        return user;
    }
}
