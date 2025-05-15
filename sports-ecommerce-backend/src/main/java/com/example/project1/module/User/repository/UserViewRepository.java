package com.example.project1.module.User.repository;

import com.example.project1.model.dto.User.UserView;
import com.example.project1.model.enity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserViewRepository extends JpaRepository<UserView, Long> {

}
