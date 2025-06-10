package com.example.project1.module.product.repository;

import com.example.project1.model.dto.view.product.ProductAttributeValueView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeValueViewRepos extends JpaRepository<ProductAttributeValueView, Long> {
}
