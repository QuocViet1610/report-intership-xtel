package com.example.project1.test.order;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.CouponDto;
import com.example.project1.model.enity.Coupon;
import com.example.project1.module.Coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/coupon")
@RequiredArgsConstructor
public class CouponCacheController {

    private final CouponCacheService couponCacheService;

    @PostMapping("")
    public ResponseResult<CouponDto> createCoupon(@RequestBody @Valid CouponDto request){
        return ResponseResult.ofSuccess(couponCacheService.create(request));
    }
    @GetMapping("")
    public ResponseResult<List<CouponDto>> createUser(){
        return ResponseResult.ofSuccess(couponCacheService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseResult<CouponDto> deleteCoupon(@PathVariable Long id){
        return ResponseResult.ofSuccess(couponCacheService.delete(id));
    }

    @PostMapping("/apply")
    public ResponseResult<String>  applyDiscountCode(@RequestParam String couponCode, @RequestParam String userLockValue) {
        boolean result = couponCacheService.applyDiscountCode(couponCode, userLockValue);

        if (result) {
            return ResponseResult.ofSuccess("Mã giảm giá đã được áp dụng thành công");
        } else {
            return ResponseResult.ofFail("Đang có người sử dụng mã giảm giá này, vui lòng thử lại sau");
        }
    }

}
