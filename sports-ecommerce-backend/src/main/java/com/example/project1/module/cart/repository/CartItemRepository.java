package com.example.project1.module.cart.repository;

import com.example.project1.model.enity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findByProductId(Long id);

    Optional<CartItem> findByProductIdAndProductVariantId(Long id, Long idVariant);
}
