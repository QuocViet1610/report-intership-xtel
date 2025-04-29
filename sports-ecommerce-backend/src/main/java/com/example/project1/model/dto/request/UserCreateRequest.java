package com.example.project1.model.dto.request;


import jakarta.persistence.Column;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
public class UserCreateRequest {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String password;

    private String facebookAccountId;

    private String googleAccountId;

    private String avatar;

    private OffsetDateTime passwordChangedAt;

    private Integer isActive;
}
