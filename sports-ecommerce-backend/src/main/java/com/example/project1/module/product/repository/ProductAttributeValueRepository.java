package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {
    List<ProductAttributeValue> findAllByAttributeValueId(Long id);
}
