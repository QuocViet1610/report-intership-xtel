package com.example.project1.module.User.repository;

import com.example.project1.model.enity.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
