package com.example.project1.module.product.repository;

import com.example.project1.model.dto.view.product.CategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryViewRepository extends JpaRepository<CategoryView, Long>, JpaSpecificationExecutor<CategoryView> {
}
