package com.example.project1.test.product;

import com.example.project1.model.dto.product.ProductDto;
import com.example.project1.model.enity.product.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    List<Object[]>  getAllNativeQuery();

    List<Product>  getAllJPA();

    Object Productspecification();

    List<Product> getProductCache();
}
