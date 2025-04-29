package com.example.project1.module.controller.product;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.BrandDto;
import com.example.project1.model.dto.product.CategoryDto;
import com.example.project1.model.dto.request.product.BrandBaseRequest;
import com.example.project1.model.dto.request.product.BrandSearchRequest;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.service.BrandService;
import com.example.project1.utils.DataUtils;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {

     BrandService brandService;


//    @PostMapping
//    public ResponseResult<CategoryDto> createCategory(@RequestBody CategoryCreateRequest request) {
//        return ResponseResult.ofSuccess(categoryService.create(request));
//    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<BrandDto> create(@ModelAttribute @TrimAndValid BrandBaseRequest createRequest) {
        return ResponseResult.ofSuccess(brandService.create(createRequest));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<BrandDto> updateCategory(@PathVariable Long id, @ModelAttribute @TrimAndValid BrandBaseRequest request) {
        return ResponseResult.ofSuccess(brandService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteCategory(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseResult.ofSuccess();
    }

    @PostMapping("/search")
    public ResponseResult<Object> search(@RequestBody @TrimAndValid BrandSearchRequest searchRequest,
                                         @PathParam("page") int page,
                                         @PathParam("size") int size,
                                         @RequestParam(name="sort", required=false) List<String> sort) {
        if (!DataUtils.isNullOrEmpty(page) && page >= 0) {
            return ResponseResult.ofSuccess(brandService.search(searchRequest, PageableCustom.setPageableCustom(page, size, sort)));
        } else {
            return ResponseResult.ofSuccess(brandService.search(searchRequest, PageableCustom.setPageableCustom(0, size, sort,true)));
        }
    }

    @GetMapping()
    public ResponseResult<List<BrandDto>> updateCategory() {
        return ResponseResult.ofSuccess(brandService.getAll());
    }
}
