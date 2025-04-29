package com.example.project1.model.dto.User;

import com.example.project1.model.dto.BaseDto;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
public class UserDto  {
    private Long id;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String fullName;

    private String email;

    private String phone;

//    private String password;

    private String facebookAccountId;

    private String googleAccountId;

    private String avatar;

    public UserDto() {

    }

    public UserDto(Long id, OffsetDateTime createdAt, OffsetDateTime updatedAt, String fullName, String email, String phone, String facebookAccountId, String googleAccountId, String avatar) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.facebookAccountId = facebookAccountId;
        this.googleAccountId = googleAccountId;
        this.avatar = avatar;
    }
}
