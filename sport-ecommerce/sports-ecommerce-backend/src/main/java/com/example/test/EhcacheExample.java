package com.example.test;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class EhcacheExample {
    public static void main(String[] args) {
        // Tạo Cache Manager
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence("cache-data"))  // Chọn nơi lưu trữ
                .build(true);

        // Tạo cache với tên 'simpleCache' và cấu hình
        Cache<String, String> cache = cacheManager.createCache("simpleCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                                ResourcePoolsBuilder.heap(10))  // Bộ nhớ đệm cho 10 phần tử
                        .build());

        // Thêm vào cache
        cache.put("user1", "User One");
        cache.put("user2", "User Two");

        // Lấy dữ liệu từ cache
        System.out.println(cache.get("user1")); // User One
        System.out.println(cache.get("user2")); // User Two

        cacheManager.close();  // Đóng CacheManager sau khi sử dụng
    }
}
