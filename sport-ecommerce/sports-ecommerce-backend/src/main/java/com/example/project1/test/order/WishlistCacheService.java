package com.example.project1.test.order;

import com.example.project1.cache.BaseCacheManager;
import com.example.project1.utils.TokenUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WishlistCacheService {

    private final BaseCacheManager baseCacheManager;
    private final TokenUtil tokenUtil;

    public void addToWishlist(String productId, String userId) {
        String key = "wishlist:" + userId;

        baseCacheManager.addToSet(key, productId);

        String countKey = "wishlist_count:" + userId;
        baseCacheManager.incrementValue(countKey, 1);
    }

    public void removeFromWishlist(String productId, String userId) {
        String key = "wishlist:" + userId;

        baseCacheManager.removeFromSet(key, productId);

        String countKey = "wishlist_count:" + userId;
        baseCacheManager.incrementValue(countKey, -1);
    }

    public long countWishlist(String userId) {
        String countKey = "wishlist_count:" + userId;

        String count = baseCacheManager.getValue(countKey);
        return count != null ? Long.parseLong(count) : 0;  // Trả về số lượng hoặc 0 nếu không có giá trị
    }

}
