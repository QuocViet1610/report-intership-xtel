package com.example.project1.module.User.service;

import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.request.AuthenticationRequest;
import com.example.project1.model.dto.request.IntrospectionRequest;
import com.example.project1.model.dto.request.RegisterRequest;
import com.example.project1.model.dto.request.VerificationOtp;
import com.example.project1.model.dto.respone.AuthenticationResponse;
import com.example.project1.model.enity.User.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    public AuthenticationResponse authenticate(AuthenticationRequest request);
    public Boolean introspection(IntrospectionRequest request) throws JOSEException, ParseException;

    public Boolean sendOtp(RegisterRequest request);

    Boolean sendOtpForgetPassword(String email);

    Boolean forgetPasswordValidate(String Email);

     String verifyForgetPassword(String email, String otp);

    Boolean changePassword(String password);

    String loginSocial(UserDto userLoginDTO);

     Boolean register(RegisterRequest request);

    UserDto verifyOtp(VerificationOtp verificationOtp);

    void logout(IntrospectionRequest request);

    String refreshToken(IntrospectionRequest request);
}
