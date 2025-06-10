package com.example.project1.module.controller.order;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.Order.AddressDto;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.request.Order.AddressCreateRequest;
import com.example.project1.module.Order.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    @PostMapping("")
    public ResponseResult<AddressDto> create(@RequestBody @TrimAndValid AddressCreateRequest createRequest) {
        return ResponseResult.ofSuccess(addressService.create(createRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseResult<AddressDto> update(@PathVariable Long id,
                                             @RequestBody @TrimAndValid AddressCreateRequest updateRequest) {
        return ResponseResult.ofSuccess(addressService.update(id, updateRequest));
    }


    @DeleteMapping("/remove/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseResult.ofSuccess();
    }


    @GetMapping("")
    public ResponseResult<List<AddressDto>> getAll() {
        return ResponseResult.ofSuccess(addressService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseResult<AddressDto> getDetail(@PathVariable Long id) {
        return ResponseResult.ofSuccess(addressService.getDetail(id));
    }
}
