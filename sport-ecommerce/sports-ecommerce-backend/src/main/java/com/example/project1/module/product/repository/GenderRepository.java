package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Long> {
}
