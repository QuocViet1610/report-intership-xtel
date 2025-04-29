package com.example.project1.module.controller.product;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.ProductDto;
import com.example.project1.model.dto.product.ProductRatingDto;
import com.example.project1.model.dto.request.product.ProductBaseRequest;
import com.example.project1.model.dto.request.product.ProductRatingCreateRequest;
import com.example.project1.model.enity.product.ProductRating;
import com.example.project1.module.product.service.ProductRatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-rating")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRatingController {

    ProductRatingService productRatingService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseResult<ProductRatingDto> create(@RequestBody @TrimAndValid ProductRatingCreateRequest createRequest) {
        return ResponseResult.ofSuccess(productRatingService.create(createRequest));
    }


}
