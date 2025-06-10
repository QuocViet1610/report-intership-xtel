package com.example.project1.module.product.repository;
import com.example.project1.model.enity.product.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {

    @Query("SELECT AVG(pr.rating) FROM ProductRating pr WHERE pr.productId = :productId")
    BigDecimal calculateAverageRating(@Param("productId") Long productId);

    List<ProductRating> findAllByProductIdAndIsActive(Long productId, Integer isActive);
}
