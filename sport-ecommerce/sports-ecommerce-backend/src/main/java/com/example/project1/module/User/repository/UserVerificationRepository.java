package com.example.project1.module.User.repository;

import com.example.project1.model.enity.User.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification,Long> {
    Optional<UserVerification> findByEmail(String gmail);
}
