package com.example.project1.module.controller.product;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.product.CategoryBaseRequest;
import com.example.project1.model.dto.request.product.CategorySearchRequest;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.service.CategoryService;
import com.example.project1.utils.DataUtils;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

     CategoryService categoryService;


//    @PostMapping
//    public ResponseResult<CategoryDto> createCategory(@RequestBody CategoryCreateRequest request) {
//        return ResponseResult.ofSuccess(categoryService.create(request));
//    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<CategoryDto> create(@ModelAttribute @TrimAndValid CategoryBaseRequest createRequest) {
        return ResponseResult.ofSuccess(categoryService.create(createRequest));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<CategoryDto> updateCategory(@PathVariable Long id, @ModelAttribute @TrimAndValid CategoryBaseRequest request) {
        return ResponseResult.ofSuccess(categoryService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseResult.ofSuccess();
    }

    @PostMapping("/search")
    public ResponseResult<Object> search(@RequestBody @TrimAndValid CategorySearchRequest searchRequest,
                                         @PathParam("page") int page,
                                         @PathParam("size") int size,
                                         @RequestParam(name="sort", required=false) List<String> sort) {
        if (!DataUtils.isNullOrEmpty(page) && page >= 0) {
            return ResponseResult.ofSuccess(categoryService.search(searchRequest, PageableCustom.setPageableCustom(page, size, sort)));
        } else {
            return ResponseResult.ofSuccess(categoryService.search(searchRequest, PageableCustom.setPageableCustom(0, size, sort,true)));
        }
    }

    @GetMapping()
    public ResponseResult<List<CategoryDto>> updateCategory() {
        return ResponseResult.ofSuccess(categoryService.findAll());
    }
}
