package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Category;
import com.example.project1.model.enity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);
    Optional<Product> findByNameAndIdNot(String name, Long id);

    @Query("SELECT MAX(p.id) FROM Product p")
    Long findMaxId();
}
