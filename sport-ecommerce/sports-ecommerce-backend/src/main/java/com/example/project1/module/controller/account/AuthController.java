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
import com.example.project1.module.User.service.AuthenticationService;
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

    AuthenticationService authenticationService;
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

    // xu ly api google
    @GetMapping("/social/callback")
    public ResponseResult<AuthenticationResponse> callback(
            @RequestParam("code") String code,
            @RequestParam("login_type") String loginType,
            HttpServletRequest request
    ) throws Exception {
        // Call the AuthService to get user info
        UserDto userLoginDTO = authService.processSocialLogin(code, loginType);
        return authService.loginSocial(userLoginDTO, request);
    }


    @PostMapping("/logout")
    public ResponseResult<Void> logout(@RequestBody IntrospectionRequest request) {
        authenticationService.logout(request);
        return ResponseResult.ofSuccess();
    }

    @PostMapping("/refresh-token")
    public ResponseResult<Void> refreshToken(@RequestBody IntrospectionRequest request) {
        authenticationService.refreshToken(request);
        return ResponseResult.ofSuccess();
    }


}
