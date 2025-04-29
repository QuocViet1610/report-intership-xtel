package com.example.project1.module.User.repository;

import com.example.project1.model.enity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userName);
    Optional<User> findByPhone(String phone);

    Optional<User> findByGoogleAccountId(String id);
}
