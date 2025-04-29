package com.example.project1.module.controller.product;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.AttributeDto;
import com.example.project1.model.dto.product.AttributeValueDTO;
import com.example.project1.model.dto.product.BrandDto;
import com.example.project1.model.dto.request.product.AttributeCreateRequest;
import com.example.project1.model.dto.request.product.AttributeSearchRequest;
import com.example.project1.model.dto.request.product.BrandBaseRequest;
import com.example.project1.model.dto.request.product.BrandSearchRequest;
import com.example.project1.module.PageableCustom;
import com.example.project1.module.product.service.AttributeService;
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
@RequestMapping("/attribute")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttributeController {

    AttributeService attributeService;

    @PostMapping("")
    public ResponseResult<AttributeDto> create(@RequestBody @TrimAndValid AttributeCreateRequest createRequest) {
        return ResponseResult.ofSuccess(attributeService.create(createRequest));
    }

    @PutMapping("/{id}")
    public ResponseResult<AttributeDto> update(@PathVariable Long id, @RequestBody @TrimAndValid AttributeCreateRequest request) {
        return ResponseResult.ofSuccess(attributeService.update(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        attributeService.delete(id);
        return ResponseResult.ofSuccess();
    }

    @PostMapping("/search")
    public ResponseResult<Object> search(@RequestBody @TrimAndValid AttributeSearchRequest searchRequest,
                                         @PathParam("page") int page,
                                         @PathParam("size") int size,
                                         @RequestParam(name="sort", required=false) List<String> sort) {
        if (!DataUtils.isNullOrEmpty(page) && page >= 0) {
            return ResponseResult.ofSuccess(attributeService.search(searchRequest, PageableCustom.setPageableCustom(page, size, sort)));
        } else {
            return ResponseResult.ofSuccess(attributeService.search(searchRequest, PageableCustom.setPageableCustom(0, size, sort, true)));
        }
    }

    @GetMapping("/all")
    public ResponseResult<List<AttributeDto>> findAll() {
        return ResponseResult.ofSuccess(attributeService.findAll());
    }

    @GetMapping("/find-value/{id}")
    public ResponseResult<List<AttributeValueDTO>> findAll(@PathVariable Long id) {
        return ResponseResult.ofSuccess(attributeService.getAttributeValue(id));
    }

}
