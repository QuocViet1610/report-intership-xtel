package com.example.project1.module.User.service.serviceImpl;

import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.mapper.User.UserVerificationMapper;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.*;
import com.example.project1.model.dto.respone.AuthenticationResponse;
import com.example.project1.model.enity.User.Role;
import com.example.project1.model.enity.User.User;
import com.example.project1.model.enity.User.UserRole;
import com.example.project1.model.enity.User.UserVerification;
import com.example.project1.module.User.repository.RoleRepository;
import com.example.project1.module.User.repository.UserRepository;
import com.example.project1.module.User.repository.UserRoleRepository;
import com.example.project1.module.User.repository.UserVerificationRepository;
import com.example.project1.module.User.service.AuthenticationService;
import com.example.project1.module.User.service.UserService;
import com.example.project1.utils.TokenUtil;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.Token;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    UserRoleRepository userRoleRepository;
    EmailService emailService;
    TokenUtil tokenUtil;
    UserVerificationRepository userVerificationRepository;
    UserVerificationService userVerificationService;
    UserService userService;
    UserVerificationMapper userVerificationMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidateException("error.user.user_name.not_exist"));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
        {
            throw new ValidateException("error.user.password.not_exist");
        }
        var token = tokenUtil.generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    @Override
    public Boolean introspection(IntrospectionRequest request) throws JOSEException, ParseException {
        return tokenUtil.validateToken(request.getToken());
    }


    public Boolean register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidateException(Translator.toMessage("Email đã được đăng ký"));
        }

        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new ValidateException(Translator.toMessage("Số điện thoại đã được đăng ký"));
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new ValidateException(Translator.toMessage("Xác nhận mật khẩu không trùng khớp"));
        }


//        String otp = userVerificationService.generateOtp();
//        emailService.sendVerificationEmail(request.getEmail(), otp);
//        userVerificationService.saveOtp(request, otp);

        return true;
    }

    public Boolean sendOtp(RegisterRequest request) {
        String otp = userVerificationService.generateOtp();
        emailService.sendVerificationEmail(request, otp);
        userVerificationService.saveOtp(request, otp);
        return true;
    }

    @Override
    public Boolean forgetPasswordValidate(String Email) {
        if (userRepository.findByEmail(Email).isEmpty()) {
            throw new ValidateException(Translator.toMessage("Email chưa được đăng ký"));
        }
        return true;
    }

    @Override
    public String verifyForgetPassword(String email, String otp) {
        UserVerification userVerification = userVerificationService.verifyOtp(email, otp);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ValidateException("error.user.user_name.not_exist"));

        String token = tokenUtil.generateToken(user);

        return token;
    }

    @Override
    public Boolean changePassword(String password) {
        Long id = tokenUtil.getCurrentUserId();
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ValidateException("error.user.user_name.not_exist"));
        user.setPassword(passwordEncoder.encode(password));
        return true;
    }

    @Override
    public Boolean sendOtpForgetPassword(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ValidateException("error.user.user_name.not_exist"));
        String otp = userVerificationService.generateOtp();
        emailService.sendEmailForgetPassword(user, otp);
        userVerificationService.saveOtpForgetPassword(email, otp);
        return true;
    }


    public UserDto verifyOtp(VerificationOtp verificationOtp) {
        UserVerification userVerification = userVerificationService.verifyOtp(verificationOtp.getEmail(), verificationOtp.getOtp());
        return userService.register(userVerification);
    }


    public String loginSocial(UserDto userLoginDTO) {
        Optional<User> optionalUser = Optional.empty();
        Role roleUser = roleRepository.findById(2L)
                .orElseThrow(() -> new ValidateException(Translator.toMessage("Quyền không tồn tại")));

        // Kiểm tra Google Account ID
        if (userLoginDTO.getGoogleAccountId() != null) {
            optionalUser = userRepository.findByGoogleAccountId(userLoginDTO.getGoogleAccountId());

            // Neu chua co thi toa moi
            if (optionalUser.isEmpty()) {
                User newUser = new User(
                        null, // id
                        Optional.ofNullable(userLoginDTO.getFullName()).orElse(""),
                        Optional.ofNullable(userLoginDTO.getEmail()).orElse(""),
                        "", // phone
                        "", // password
                        null, // facebookAccountId
                        userLoginDTO.getGoogleAccountId(),
                        Optional.ofNullable(userLoginDTO.getAvatar()).orElse(""),
                        null, // passwordChangedAt
                        1, // isActive
                        OffsetDateTime.now(), // createdAt
                        OffsetDateTime.now(), // updatedAt
                        new HashSet<>() // productRatings
                );


                // Lưu người dùng mới
                newUser = userRepository.save(newUser);
                optionalUser = Optional.of(newUser);
                UserRole userRole = new UserRole();
                userRole.setRoleId(2L);
                userRole.setUserId(optionalUser.get().getId());
                userRoleRepository.save(userRole);
            }
        }
        // Kiểm tra Facebook Account ID
//        else if (userLoginDTO.isFacebookAccountIdValid()) {
//            optionalUser = userRepository.findByFacebookAccountId(userLoginDTO.getFacebookAccountId());
//
//            // Tạo người dùng mới nếu không tìm thấy
//            if (optionalUser.isEmpty()) {
//                User newUser = User.builder()
//                        .fullName(Optional.ofNullable(userLoginDTO.getFullname()).orElse(""))
//                        .email(Optional.ofNullable(userLoginDTO.getEmail()).orElse(""))
//                        .profileImage(Optional.ofNullable(userLoginDTO.getProfileImage()).orElse(""))
//                        .role(roleUser)
//                        .facebookAccountId(userLoginDTO.getFacebookAccountId())
//                        .password("") // Mật khẩu trống cho đăng nhập mạng xã hội
//                        .active(true)
//                        .build();
//
//                // Lưu người dùng mới
//                newUser = userRepository.save(newUser);
//                optionalUser = Optional.of(newUser);
//            }
//        } else {
//            throw new IllegalArgumentException("Invalid social account information.");
//        }

        User user = optionalUser.get();

        // Kiểm tra nếu tài khoản bị khóa
        if (user.getIsActive() == 0) {
            throw new ValidateException(Translator.toMessage("Tài khoản đã bị khoá"));
        }

        // Tạo JWT token cho người dùng
        return tokenUtil.generateToken(user);
    }

}
