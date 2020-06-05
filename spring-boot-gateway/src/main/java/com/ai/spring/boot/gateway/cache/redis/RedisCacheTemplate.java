package com.ai.spring.boot.gateway.cache.redis;

import com.ai.spring.boot.gateway.cache.CacheTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存操作redis实现
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Service
@Slf4j
public class RedisCacheTemplate implements CacheTemplate {
    public final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public <T> T get(String cachekey,Class<T> rtnType) throws IOException {

        String cacheVal = redisTemplate.opsForValue().get(cachekey);
        if (cacheVal == null){
            return null;
        }
        return mapper.readValue(cacheVal,rtnType);
    }
    @Override
    public <T> List<T> getList(String cachekey,Class<T> rtnType) throws IOException {

        String cacheVal = redisTemplate.opsForValue().get(cachekey);
        if (cacheVal == null){
            return null;
        }
        JavaType javaType = getCollectionType(List.class, rtnType);
        return mapper.readValue(cacheVal, javaType);
    }
    private JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
    @Override
    public <T> void set2Cache(String cachekey, T data, boolean isarray,int expireSec) throws JsonProcessingException {

        if (data==null){
            return;
        }
        String cacheVal = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(cachekey,cacheVal,expireSec, TimeUnit.SECONDS);
    }

    @Override
    public Boolean remove(String cachekey) {
        return redisTemplate.delete(cachekey);
    }
}
