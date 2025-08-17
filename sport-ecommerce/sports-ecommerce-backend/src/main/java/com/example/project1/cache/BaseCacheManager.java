package com.example.project1.cache;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface BaseCacheManager {
    <T> List<T> getList(String key, Class zClass);

    void delete(String key);

    void setCache(String key, Object object, int time, TimeUnit timeUnit);

    void setCache(String key, List object);

    public void setCache(String key, Object object);

    <T> Object getObject(String key, Class zClass);

    void  deleteKeys(Set<String> keys);

    Set<String> getKeys(String pattern );

    void incrementValue(String key, long delta);

    void addToSet(String key, String value);


    void removeFromSet(String key, String value);

    Set<String> getSet(String key);

    public String getValue(String key);

    boolean getBit(String key, long offset);
    void setBit(String key, long offset, boolean value);

}
