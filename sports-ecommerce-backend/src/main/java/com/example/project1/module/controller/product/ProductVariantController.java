package com.example.project1.module.controller.product;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.module.product.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-variant")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantController {
    ProductVariantService productVariantService;
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteCategory(@PathVariable Long id) {
        productVariantService.delete(id);
        return ResponseResult.ofSuccess();
    }
}
