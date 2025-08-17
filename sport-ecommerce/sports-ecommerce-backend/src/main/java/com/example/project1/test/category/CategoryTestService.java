package com.example.project1.test.category;

import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.product.CategoryCreateRequest;

import java.util.List;

public interface CategoryTestService {

    List<CategoryDto> getAll();

    CategoryDto create(CategoryCreateRequest createRequest);
    CategoryDto update(Long id, CategoryCreateRequest request);

    void delete(Long id);
}
