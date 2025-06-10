package com.example.project1.module.controller.product;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.request.product.ProductVariantCreateRequest;
import com.example.project1.model.dto.request.product.ProductVariantUpdate;
import com.example.project1.model.dto.respone.CartResponse;
import com.example.project1.model.enity.product.ProductVariant;
import com.example.project1.module.product.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseResult<ProductVariant> update(@PathVariable Long id
                                       ,@RequestBody @TrimAndValid ProductVariantUpdate request) {
        return ResponseResult.ofSuccess(productVariantService.update(id, request));
    }

    @GetMapping("/detail/{id}")
    public ResponseResult<ProductVariant> getCartByUserId(@PathVariable Long id) {
        return ResponseResult.ofSuccess(productVariantService.getDetail(id));
    }

}
