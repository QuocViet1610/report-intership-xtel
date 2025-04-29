package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.ProductVariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductVariantAttributeRepository extends JpaRepository<ProductVariantAttribute, Long>, JpaSpecificationExecutor<ProductVariantAttribute> {

}
