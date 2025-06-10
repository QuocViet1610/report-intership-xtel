package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long>{
    List<AttributeValue> findAllByAttributeId(Long id);
}
