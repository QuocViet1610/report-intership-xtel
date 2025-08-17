package com.example.project1.test.customers;

import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.product.CouponDto;
import com.example.project1.test.customers.service.CustomerService;
import com.example.project1.test.order.CouponCacheService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping("")
    public ResponseResult<List<Customer>> get(){
        return ResponseResult.ofSuccess(customerService.getAll());
    }

    @GetMapping("/navtive")
    public ResponseResult<List<CustomerDto>> getNative(){
        return ResponseResult.ofSuccess(customerService.getNative());
    }

    @GetMapping("/navtive-cache")
    public ResponseResult<List<CustomerDto>> getNativeCache(){
        return ResponseResult.ofSuccess(customerService.getNativeCache());
    }

    @GetMapping("/page")
    public Page<Customer> getCustomers(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "1000") int size) {
        return customerService.getAllCustomers(page, size);
    }

    @GetMapping("/page-native")
    public Page<Customer> getCustomersNative(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "1000") int size) {
        return customerService.getAllCustomersNative(page, size);
    }

    @GetMapping("/check-user-bitmap")
    public ResponseEntity<String> checkUserExistsCache(@RequestParam("user") String username) {
        if (customerService.checkUserCache(username)) {
            return ResponseEntity.ok("Người dùng đã tồn tại trong cache.");
        } else {
            return ResponseEntity.ok("Người dùng chưa tồn tại trong cache.");
        }
    }

    @GetMapping("/check-user")
    public ResponseEntity<String> checkUserExists(@RequestParam("user") String username) {
        if (customerService.checkUser(username)) {
            return ResponseEntity.ok("Người dùng đã tồn tại trong cache.");
        } else {
            return ResponseEntity.ok("Người dùng chưa tồn tại trong cache.");
        }
    }

}
