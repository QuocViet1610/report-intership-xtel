package com.example.project1.module.controller.Cart;

import com.example.project1.middleware.annotation.TrimAndValid;
import com.example.project1.model.dto.ResponseResult;
import com.example.project1.model.dto.cart.CartItemCreateRequest;
import com.example.project1.model.dto.cart.CartItemDto;
import com.example.project1.model.dto.respone.CartResponse;
import com.example.project1.module.cart.service.CartService;
import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;


    @PostMapping("")
    public ResponseResult<CartItemDto> addProductToCart(@RequestBody @TrimAndValid CartItemCreateRequest createRequest) {
        return ResponseResult.ofSuccess(cartService.addProductToCart(createRequest));
    }


    @PutMapping("/update/{cartItemId}")
    public ResponseResult<CartItemDto> updateQuantity(@PathVariable Long cartItemId, @RequestParam Long quantity) {
        return ResponseResult.ofSuccess(cartService.updateQuantity(cartItemId, quantity));
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseResult<Void> removeProductFromCart(@PathVariable Long cartItemId) {
        cartService.removeProductFromCart(cartItemId);
        return ResponseResult.ofSuccess();
    }


    @GetMapping("")
    public ResponseResult<CartResponse> getCartByUserId() {
        return ResponseResult.ofSuccess(cartService.getCartByUserId());
    }


//    @PostMapping("/search")
//    public ResponseResult<Object> searchCart(@RequestBody @TrimAndValid CartItemCreateRequest searchRequest,
//                                             @PathParam("page") int page,
//                                             @PathParam("size") int size,
//                                             @RequestParam(name="sort", required=false) List<String> sort) {
//        if (!DataUtils.isNullOrEmpty(page) && page >= 0) {
//            return ResponseResult.ofSuccess(cartService.searchCart(searchRequest, PageableCustom.setPageableCustom(page, size, sort)));
//        } else {
//            return ResponseResult.ofSuccess(cartService.searchCart(searchRequest, PageableCustom.setPageableCustom(0, size, sort, true)));
//        }
//    }
}
