package com.example.project1.module.controller.account;
import com.example.project1.expection.ValidateException;
import com.example.project1.local.Translator;
import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.AuthenticationRequest;
import com.example.project1.model.dto.request.IntrospectionRequest;
import com.example.project1.model.dto.request.RegisterRequest;
import com.example.project1.model.dto.request.VerificationOtp;
import com.example.project1.model.dto.respone.AuthenticationResponse;
import com.example.project1.module.User.service.serviceImpl.AuthenticationServiceImpl;
import com.example.project1.module.auth.AuthService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthenticationServiceImpl authenticationService;
    AuthService authService;

    @PostMapping("/login")
    ResponseResult<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws MessagingException {
        var result = authenticationService.authenticate(request);
        return ResponseResult.ofSuccess(result);
    }

    @PostMapping("/introspect")
    ResponseResult<Boolean> authenticate(@RequestBody IntrospectionRequest request)
            throws ParseException, JOSEException {
        return ResponseResult.ofSuccess(authenticationService.introspection(request));

    }

    @PostMapping("/register")
    public ResponseResult<Boolean> register(@RequestBody @TrimAndValid RegisterRequest request) {
        return ResponseResult.ofSuccess(authenticationService.register(request));
    }

    @PostMapping("/send-otp")
    public ResponseResult<Boolean> sendOtp(@RequestBody @TrimAndValid RegisterRequest request) {
        return ResponseResult.ofSuccess(authenticationService.sendOtp(request));
    }

    @PostMapping("/verify-otp")
    public ResponseResult<UserDto> verifyOtp(@RequestBody VerificationOtp verificationOtp) {
        return ResponseResult.ofSuccess(authenticationService.verifyOtp(verificationOtp));
    }


    @PostMapping("/validate-forget-password/{email}")
    public ResponseResult<Boolean> validateForgetPassword(@PathVariable String email) {
        return ResponseResult.ofSuccess(authenticationService.forgetPasswordValidate(email));
    }

    //xac nhan otp
    @PostMapping("/verify-forget-password/{email}/{otp}")
    public ResponseResult<String> verifyForgetPassword(@PathVariable String email, @PathVariable String otp) {
        return ResponseResult.ofSuccess(authenticationService.verifyForgetPassword(email, otp));
    }

    // doi mat khau
    @PostMapping("/change-password/{password}")
    public ResponseResult<Boolean> changePassword(@PathVariable String password) {
        return ResponseResult.ofSuccess(authenticationService.changePassword(password));
    }

    //gui otp quen mat khau
    @PostMapping("/send-otp-forget-password/{email}")
    public ResponseResult<Boolean> sendOtpForgetPassword(@PathVariable String email) {
        return ResponseResult.ofSuccess(authenticationService.sendOtpForgetPassword(email));
    }

    @GetMapping("/social-login")
    public ResponseResult<String> socialAuth(
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ) {
        //request.getRequestURI()
        loginType = loginType.trim().toLowerCase();  // Loại bỏ dấu cách và chuyển thành chữ thường
        String url = authService.generateAuthUrl(loginType);
        return ResponseResult.ofSuccess(url);
    }

    // xu ly api gg
    @GetMapping("/social/callback")
    public ResponseResult<AuthenticationResponse> callback(
            @RequestParam("code") String code,
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ) throws Exception {
        // Call the AuthService to get user info
        Map<String, Object> userInfo = authService.authenticateAndFetchProfile(code, loginType);

        if (userInfo == null) {
            new ValidateException(Translator.toMessage("Đăng nhập thất bại"));
        }

        // lay thong tin cua google
        String accountId = "";
        String name = "";
        String picture = "";
        String email = "";

        if (loginType.trim().equals("google")) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("sub"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            picture = (String) Objects.requireNonNullElse(userInfo.get("picture"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
        } else if (loginType.trim().equals("facebook")) {
            accountId = (String) Objects.requireNonNullElse(userInfo.get("id"), "");
            name = (String) Objects.requireNonNullElse(userInfo.get("name"), "");
            email = (String) Objects.requireNonNullElse(userInfo.get("email"), "");
            // Lấy URL ảnh từ cấu trúc dữ liệu của Facebook
            Object pictureObj = userInfo.get("picture");
            if (pictureObj instanceof Map) {
                Map<?, ?> pictureData = (Map<?, ?>) pictureObj;
                Object dataObj = pictureData.get("data");
                if (dataObj instanceof Map) {
                    Map<?, ?> dataMap = (Map<?, ?>) dataObj;
                    Object urlObj = dataMap.get("url");
                    if (urlObj instanceof String) {
                        picture = (String) urlObj;
                    }
                }
            }
        }

        // Tạo đối tượng UserLoginDTO
        UserDto userLoginDTO = new UserDto(
                null,                            // id
                null,                            // createdAt
                null,                            // updatedAt
                name,                            // fullName
                email,                           // email
                "",                              // phone
                "",                               // facebookAccountId
                "",                               // googleAccountId
                picture                          // avatar
        );


        if (loginType.trim().equals("google")) {
            userLoginDTO.setGoogleAccountId(accountId);
            //userLoginDTO.setFacebookAccountId("");
        } else if (loginType.trim().equals("facebook")) {
            userLoginDTO.setFacebookAccountId(accountId);
            //userLoginDTO.setGoogleAccountId("");
        }

        return this.loginSocial(userLoginDTO, request);
    }

    private ResponseResult<AuthenticationResponse> loginSocial(
            @Valid @RequestBody UserDto userLoginDTO,
            HttpServletRequest request
    ) {
        // Gọi hàm loginSocial từ UserService cho đăng nhập mạng xã hội
        String token = authenticationService.loginSocial(userLoginDTO);
        var result = AuthenticationResponse.builder().token(token).authenticated(true).build();
        return ResponseResult.ofSuccess(result);
    }

}
