package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Attribute;
import com.example.project1.model.enity.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute> {
    Optional<Attribute> findByName(String name);

    Optional<Attribute> findByNameAndIdNot(String name, Long id);

    @Query("SELECT MAX(a.displayOrder) FROM Attribute a")
    Optional<Integer> findMaxDisplayOrder();

    @Query("SELECT a FROM Attribute a LEFT JOIN FETCH a.values")
    List<Attribute> findAllWithValues();
}
