package com.example.project1.module.product.repository;
import com.example.project1.model.enity.product.Category;
import com.example.project1.model.enity.product.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRatingRepository extends JpaRepository<ProductRating, Long> {

}
