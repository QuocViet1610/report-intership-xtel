package com.example.project1.module.User.repository;

import com.example.project1.model.enity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userName);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailAndIdNot(String userName, Long id);
    Optional<User> findByPhoneAndIdNot(String phone, Long id);

    Optional<User> findByGoogleAccountId(String id);

    @Query("SELECT u FROM User u JOIN UserRole ur ON u.id = ur.userId JOIN Role r ON ur.role.id = r.id WHERE r.id = 1")
    List<User> findUsersByRoleId();

    @Query("SELECT u FROM User u JOIN UserRole ur ON u.id = ur.userId JOIN Role r ON ur.role.id = r.id WHERE r.id = 2")
    List<User> findUsersByRoleIdUser();
}
