package com.ai.spring.boot.gateway.cache;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

/**
 * 缓存操作接口
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
public interface CacheTemplate {
    /**从缓存中获取数据*/
    <T>T get(String cachekey,Class<T> rtnType) throws IOException;
    <T> List<T> getList(String cachekey, Class<T> rtnType) throws IOException;
    /**添加数据到缓存中*/
    <T> void set2Cache(String cachekey,T data,boolean isarray,int expireSec) throws JsonProcessingException;
    /**清除缓存*/
    Boolean remove(String cachekey);
}
