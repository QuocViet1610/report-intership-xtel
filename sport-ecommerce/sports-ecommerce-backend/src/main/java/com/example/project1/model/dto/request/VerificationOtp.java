package com.example.project1.model.dto.request;

import com.example.project1.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerificationOtp {
    @NotBlank(message = "error.user.email.required")
    @Pattern(message = "error.user.email.invalid", regexp = Constants.REGEX.EMAIL_PATTERN)
    private String email;

    @NotBlank(message = "error.otp.required")
    @Size(min = 6, max = 6, message = "error.otp.length")
    private String otp;
}
