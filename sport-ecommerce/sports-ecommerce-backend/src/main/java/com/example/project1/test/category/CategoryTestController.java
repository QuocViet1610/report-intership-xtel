package com.example.project1.test.category;


import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.product.CategoryCreateRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryTestController {
    CategoryTestService categoryService;

    @GetMapping()
    public ResponseResult<List<CategoryDto>> get() {
        return ResponseResult.ofSuccess(categoryService.getAll());
    }

    @PostMapping()
    public ResponseResult<CategoryDto> get(@RequestBody @Valid CategoryCreateRequest createRequest) {
        return ResponseResult.ofSuccess(categoryService.create(createRequest));
    }

}
