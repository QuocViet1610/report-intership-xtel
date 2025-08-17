package com.example.project1.module.controller.Blog;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.blog.BlogDto;
import com.example.project1.model.dto.request.Blog.BlogBaseRequest;
import com.example.project1.model.dto.request.Blog.BlogSearch;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.blog.BlogService;
import com.example.project1.utils.DataUtils;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
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
    public ResponseResult<Void> updateQuantity(@RequestBody @TrimAndValid BlogDto createRequest, @PathVariable Long id) {
        blogService.updateBlog(createRequest, id);
        return ResponseResult.ofSuccess();
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

    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleted(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseResult.ofSuccess();
    }

    @GetMapping("/get-detail/{id}")
    public ResponseResult<Object> getDetail(@PathVariable Long id) {
        return ResponseResult.ofSuccess(blogService.getDetail(id));
    }
}
