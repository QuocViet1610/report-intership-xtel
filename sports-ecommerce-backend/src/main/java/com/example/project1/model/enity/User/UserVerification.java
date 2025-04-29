package com.example.project1.model.enity.User;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_verification")
@Data
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 6)
    private String otp;

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;
    @Column(name = "failed_attempts")
    private int failedAttempts = 0; // Số lần nhập sai

    @Column(name = "lock_time")
    private LocalDateTime lockTime; // Thời gian bị khóa do nhập sai quá nhiều

    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;


    public boolean isLocked() {
        return lockTime != null && lockTime.isAfter(LocalDateTime.now());
    }
    public UserVerification() {

    }
}
