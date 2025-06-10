package com.example.project1.model.dto.request;

import com.example.project1.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @NotBlank(message = "error.user.email.required")
    @Pattern(message = "error.user.email.invalid", regexp = Constants.REGEX.EMAIL_PATTERN)
    private String email;

    @NotBlank(message = "error.user.password.required")
    private String password;
}
