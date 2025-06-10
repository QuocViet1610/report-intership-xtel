package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findByIdAndProductId(Long id, Long productId);
}
