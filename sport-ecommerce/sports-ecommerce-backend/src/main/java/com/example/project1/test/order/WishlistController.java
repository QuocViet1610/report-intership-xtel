package com.example.project1.test.order;
import com.example.project1.model.dto.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistCacheService wishlistService;

    @PostMapping("/add")
    public ResponseResult<String> addToWishlist(@RequestParam String productId, @RequestParam String userId) {
        wishlistService.addToWishlist(productId, userId);
        return ResponseResult.ofSuccess("Sản phẩm đã được thêm vào wishlist.");
    }

    @PostMapping("/remove")
    public ResponseResult<String> removeFromWishlist(@RequestParam String productId, @RequestParam String userId) {
        wishlistService.removeFromWishlist(productId, userId);
        return ResponseResult.ofSuccess("Sản phẩm đã được xóa khỏi wishlist.");
    }

    @GetMapping("/count")
    public ResponseResult<Long> countWishlist(@RequestParam String userId) {
        long count = wishlistService.countWishlist(userId);
        return ResponseResult.ofSuccess(count);
    }
}
