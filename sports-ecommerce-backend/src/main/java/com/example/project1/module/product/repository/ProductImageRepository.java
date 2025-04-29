package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Category;
import com.example.project1.model.enity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
    List<ProductImage> findAllByProductId(Long id);
}
