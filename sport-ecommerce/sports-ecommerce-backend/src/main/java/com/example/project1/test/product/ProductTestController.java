package com.example.project1.test.product;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.enity.product.Product;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductTestController {

     ProductService productService;

    @GetMapping()
    public ResponseResult<List<Product>> get() {
        return ResponseResult.ofSuccess(productService.getAll());
    }

    @GetMapping("/nativeQuery")
    public ResponseResult<List<Object[]> > getNativeQuery() {
        return ResponseResult.ofSuccess(productService.getAllNativeQuery());
    }

    @GetMapping("/get-JPQL")
    public ResponseResult<List<Product>> getJPQL() {
        return ResponseResult.ofSuccess(productService.getAllJPA());
    }

    @GetMapping("/get-specification")
    public ResponseResult<Object > getSpecification() {
        return ResponseResult.ofSuccess(productService.Productspecification());
    }

    @GetMapping("/get-product-cache")
    public ResponseResult<Object > getProductCache() {
        return ResponseResult.ofSuccess(productService.getProductCache());
    }
}
