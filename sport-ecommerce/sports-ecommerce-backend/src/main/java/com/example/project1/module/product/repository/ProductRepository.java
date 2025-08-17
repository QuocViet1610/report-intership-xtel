package com.example.project1.module.product.repository;

import com.example.project1.model.dto.product.ProductDto;
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

    //Test Efficiency

    @Query(value = "SELECT * FROM product", nativeQuery = true)
    List<Product> findAllProducts();

    @Query(value = "SELECT * FROM product p " +
            "LEFT JOIN product_variants pv ON p.id = pv.product_id " +
            "LEFT JOIN product_image pi ON p.id = pi.product_id " +
            "LEFT JOIN product_attribute pa ON p.id = pa.product_id " +
            "LEFT JOIN product_rating pr ON p.id = pr.product_id " +
            "WHERE p.is_active = 1", nativeQuery = true)
    List<Object[]> findProductRevenueAndAvgRatingByCategory();

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.productVariants pv " +
            "LEFT JOIN FETCH p.productImages pi " +
            "LEFT JOIN FETCH p.productAttributes pa " +
            "LEFT JOIN FETCH p.productRatings pr " +
            "WHERE p.isActive = 1")
    List<Product> findProductRevenueAndAvgRatingByCategoryJPQL();


    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.productVariants")
    List<Product> findAllWithVariants();
}
