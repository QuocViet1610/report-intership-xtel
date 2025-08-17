package com.example.project1.cache;

import com.example.project1.utils.Constants;
import com.example.project1.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

@Service(value = "baseCacheManager")
@RequiredArgsConstructor
public class BaseCacheManagerImpl implements BaseCacheManager {

    private final RedisUtil<String> redisUtil;
    private static final String USER_BITMAP_KEY = "user:bitmap";


    @Override
    public <T> List<T> getList(String key, Class zClass) {
        String jsonString = redisUtil.getValue(key);
        List<T> objects = new ArrayList<>();
        if (!DataUtils.isNullOrEmpty(jsonString)){
            objects = DataUtils.jsonToListThrow(jsonString, zClass, "error.common.get.cache");
        }
        return objects;
    }

    @Override
    public Set<String> getKeys(String pattern) {
        return redisUtil.getKeys(pattern);
    }

    @Override
    public void incrementValue(String key, long delta) {
        redisUtil.incrementValue(key, delta);
    }

    @Override
    public void addToSet(String key, String value) {
        redisUtil.addToSet(key, value);
    }

    @Override
    public void removeFromSet(String key, String value) {
        redisUtil.removeFromSet(key,value);
    }

    @Override
    public Set<String> getSet(String key) {
        return redisUtil.getSet(key);
    }

    @Override
    public void delete(String key) {
        redisUtil.delValue(key);
    }

    @Override
    public void deleteKeys(Set<String> keys) {
        redisUtil.delKeys(keys);
    }

    @Override
    public void setCache(String key, Object object, int time, TimeUnit timeUnit) {
        redisUtil.putValueWithExpireTime(key, DataUtils.objectToJson(object), time, timeUnit);
    }

    @Override
    public void setCache(String key, List object) {
        redisUtil.putValueWithExpireTime(key, DataUtils.objectToJson(object), Constants.timeOutCache30Days, TimeUnit.DAYS);
    }

    @Override
    public void setCache(String key, Object object) {
        redisUtil.putValueWithExpireTime(key, DataUtils.objectToJson(object), Constants.timeOutCache30Days, TimeUnit.DAYS);
    }
    @Override
    public <T> Object getObject(String key, Class zClass) {
        String jsonString = redisUtil.getValue(key);
        if (!DataUtils.isNullOrEmpty(jsonString)){
            return DataUtils.jsonToObject(jsonString, zClass);
        }
        return null;
    }

    public String getValue(String key){
        return redisUtil.getValue(key);
    }

    private long hashUsernameToOffset(String username) {
        CRC32 crc = new CRC32();
        crc.update(username.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();  // offset từ 0 đến 2^32
    }

    @Override
    public boolean getBit(String key, long offset) {
        return redisUtil.getBit(key, offset);
    }

    @Override
    public void setBit(String key, long offset, boolean value) {
        redisUtil.setBit(key, offset, value);
    }


}
