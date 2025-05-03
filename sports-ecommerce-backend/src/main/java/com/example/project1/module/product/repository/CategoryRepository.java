package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    List<Category> findByName(String name);
    Optional<Category> findByNameAndIdNot(String name, Long id);
}
