package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Category;
import com.example.project1.model.enity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);
    Optional<Product> findByNameAndIdNot(String name, Long id);

    @Query("SELECT MAX(p.id) FROM Product p")
    Long findMaxId();

    List<Product> findAllByCategoryId(Long id);

    @Query(value = "SELECT * FROM product WHERE category_id IN :categoryIds", nativeQuery = true)
    List<Product> findAllByCategoryId(@Param("categoryIds") List<Long> categoryIds);

    List<Product> findByBrandId(Long id);

}
