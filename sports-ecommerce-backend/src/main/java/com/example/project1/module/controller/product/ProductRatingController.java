package com.example.project1.module.controller.product;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.ProductRatingSearch;
import com.example.project1.model.dto.request.product.ProductRatingCreateRequest;
import com.example.project1.model.dto.request.product.ProductSearchRequest;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.service.ProductRatingService;
import com.example.project1.utils.DataUtils;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-rating")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRatingController {

    ProductRatingService productRatingService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseResult<Object> create(@RequestBody @TrimAndValid ProductRatingCreateRequest createRequest) {
        return ResponseResult.ofSuccess(productRatingService.create(createRequest));
    }

    @GetMapping("/{id}")
    public ResponseResult<Object> getDetail(@PathVariable Long id) {
        return ResponseResult.ofSuccess(productRatingService.getAllByProduct(id));
    }

    @PostMapping("/search")
    public ResponseResult<Object> search(@RequestBody @TrimAndValid ProductRatingSearch searchRequest,
                                         @PathParam("page") int page,
                                         @PathParam("size") int size,
                                         @RequestParam(name="sort", required=false) List<String> sort) {
        if (!DataUtils.isNullOrEmpty(page) && page >= 0) {
            return ResponseResult.ofSuccess(productRatingService.search(searchRequest, PageableCustom.setPageableCustom(page, size, sort)));
        } else {
            return ResponseResult.ofSuccess(productRatingService.search(searchRequest, PageableCustom.setPageableCustom(0, size, sort,true)));
        }
    }
}
