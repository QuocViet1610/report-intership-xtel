package com.example.project1.module.product.repository;

import com.example.project1.model.dto.view.product.ProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductViewRepository extends JpaRepository<ProductView, Long>, JpaSpecificationExecutor<ProductView> {

}
