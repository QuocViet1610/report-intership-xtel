package com.example.project1.module.product.repository;

import com.example.project1.model.enity.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    Optional<Brand> findByName(String name);

    Optional<Brand> findByNameAndIdNot(String name, Long id);
}
