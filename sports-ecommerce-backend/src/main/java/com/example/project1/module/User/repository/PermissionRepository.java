package com.example.project1.module.User.repository;

import com.example.project1.model.enity.User.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {

}
