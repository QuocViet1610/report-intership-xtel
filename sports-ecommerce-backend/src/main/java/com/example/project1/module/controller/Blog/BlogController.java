package com.example.project1.module.controller.Blog;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.config.MinioConfig;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.blog.BlogDto;
import com.example.project1.model.dto.cart.CartItemCreateRequest;
import com.example.project1.model.dto.cart.CartItemDto;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.Blog.BlogBaseRequest;
import com.example.project1.model.dto.request.Blog.BlogSearch;
import com.example.project1.model.dto.request.Order.OrderSearchRequest;
import com.example.project1.model.dto.request.product.CategoryBaseRequest;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.blog.BlogService;
import com.example.project1.utils.DataUtils;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<Object> create(@ModelAttribute @TrimAndValid BlogBaseRequest createRequest) {
        return ResponseResult.ofSuccess(blogService.create(createRequest));
    }

//    @PostMapping("")
//    public ResponseResult<Object> addProductToCart(@RequestBody @TrimAndValid BlogDto createRequest) {
//        return ResponseResult.ofSuccess(blogService.createBlog(createRequest));
//    }


    @PutMapping("/{id}")
    public ResponseResult<Object> updateQuantity(@RequestBody @TrimAndValid BlogDto createRequest, @RequestParam Long id) {
        return ResponseResult.ofSuccess(blogService.updateBlog(createRequest, id));
    }

    @PostMapping("/search")
    public ResponseResult<Object> search(@RequestBody @TrimAndValid BlogSearch searchRequest,
                                         @PathParam("page") int page,
                                         @PathParam("size") int size,
                                         @RequestParam(name="sort", required=false) List<String> sort) {
        if (!DataUtils.isNullOrEmpty(page) && page >= 0) {
            return ResponseResult.ofSuccess(blogService.search(searchRequest, PageableCustom.setPageableCustom(page, size, sort)));
        } else {
            return ResponseResult.ofSuccess(blogService.search(searchRequest, PageableCustom.setPageableCustom(0, size, sort,true)));
        }
    }

}
