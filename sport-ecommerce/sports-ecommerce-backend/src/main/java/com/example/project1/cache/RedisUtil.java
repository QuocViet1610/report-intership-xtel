package com.example.project1.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class RedisUtil<T> {
    private final RedisTemplate<String, T> redisTemplate; // class thao tac redis

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Boolean tryLock(String lockKey, String lockValue, long timeout, TimeUnit unit) {
        Boolean lockAcquired = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, timeout, unit);
        return lockAcquired;
    }

    // Phương thức để giải phóng khóa
    public Boolean releaseLock(String lockKey, String lockValue) {
        // Đảm bảo chỉ xóa khóa nếu lockValue chính xác, tránh giải phóng khóa của người khác
        return stringRedisTemplate.execute(new DefaultRedisScript<>(
                        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                                "return redis.call('del', KEYS[1])" + // nếu lockKey == lockValue thì xóa khóa
                                " else " +
                                "return 0 end", Long.class),
                List.of(lockKey), lockValue) == 1; // Trả về 1 nếu khóa được xóa thành công
    }

    public void putValueWithExpireTime(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public T getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delValue(String key) {
        return redisTemplate.delete(key);
    }

    public void delKeys(Set<String> keys) {
        redisTemplate.delete(keys);
    }
    public Set<String> getKeys(String pattern ){
        return redisTemplate.keys(pattern);
    }


    public void boundHashOps(String redisKey, String key, T value) { // hash type, object
        redisTemplate.opsForHash().put(redisKey, key, value);
    }

    public List<Object> getBoundHashOps(String redisKey) { // hash type, object
        return redisTemplate.boundHashOps(redisKey).values();
    }

    public void incrementValue(String key, long delta) {
        stringRedisTemplate.opsForValue().increment(key, delta);
    }

    public void addToSet(String key, String value) {
        // Sử dụng Redis để thêm value vào Set (set dữ liệu kiểu Set<String>)
        stringRedisTemplate.opsForSet().add(key, value);
    }

    public void removeFromSet(String key, String value) {
        // Sử dụng Redis để xóa value khỏi Set
        stringRedisTemplate.opsForSet().remove(key, value);
    }

    public Set<String> getSet(String key) {
        // Lấy tất cả phần tử trong Set từ Redis
        return stringRedisTemplate.opsForSet().members(key);
    }

    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    public void setBit(String key, long offset, boolean value) {
        redisTemplate.opsForValue().setBit(key, offset, value);
    }
}
