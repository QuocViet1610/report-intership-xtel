package com.example.project1.model.dto.request;

import com.example.project1.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;


@Data
public class RegisterRequest {

    @NotBlank(message = "error.user.fullname.required")
    @Size(min = 3, max = 50, message = "error.user.fullname.size")
    private String fullName;

    @NotBlank(message = "error.user.email.required")
    @Pattern(message = "error.user.email.invalid", regexp = Constants.REGEX.EMAIL_PATTERN)
    private String email;

    @NotBlank(message = "error.user.phone.required")
    @Pattern(regexp = Constants.REGEX.PHONE_PATTERN, message = "error.user.phone.invalid")
    private String phone;

    @NotBlank(message = "error.user.password.required")
    @Size(min = 8, message = "error.user.password.size")
    private String password;

    @NotBlank(message = "error.user.passwordConfirm.required")
    private String passwordConfirm;

}
