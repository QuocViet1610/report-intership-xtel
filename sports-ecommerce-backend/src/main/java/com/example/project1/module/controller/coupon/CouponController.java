package com.example.project1.module.controller.coupon;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.User.UserDto;
import com.example.project1.model.dto.product.CouponDto;
import com.example.project1.model.dto.request.UserCreateRequest;
import com.example.project1.model.enity.Coupon;
import com.example.project1.module.Coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    @PostMapping("")
    public ResponseResult<Coupon> createUser(@RequestBody @Valid CouponDto request){
        return ResponseResult.ofSuccess(couponService.create(request));
    }
    @GetMapping("")
    public ResponseResult<List<Coupon>> createUser(){
        return ResponseResult.ofSuccess(couponService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseResult<Coupon> createUser(@PathVariable Long id){
        return ResponseResult.ofSuccess(couponService.delete(id));
    }

    @GetMapping("/detail")
    public ResponseResult<Coupon> createUser(@RequestParam String code) {
        return ResponseResult.ofSuccess(couponService.getDetailByCode(code));
    }

}
