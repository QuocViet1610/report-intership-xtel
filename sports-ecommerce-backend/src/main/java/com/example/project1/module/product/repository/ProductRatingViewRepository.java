package com.example.project1.module.product.repository;

import com.example.project1.model.dto.view.product.CategoryView;
import com.example.project1.model.enity.product.ProductRatingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.swing.text.html.Option;
import java.util.List;

public interface ProductRatingViewRepository extends JpaRepository<ProductRatingView, Long>, JpaSpecificationExecutor<ProductRatingView> {

    List<ProductRatingView> findAllByProductIdAndIsActive(Long id, Integer isActice);

}
