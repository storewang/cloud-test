package com.ai.spring.boot.netty.ws.service.impl;

import com.ai.spring.boot.netty.ws.service.RegistHostService;
import com.ai.spring.boot.netty.ws.util.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 服务机器注册服务(redis)
 *
 * @author 石头
 * @Date 2020/7/2
 * @Version 1.0
 **/
@Service
public class RedisRegistHostService implements RegistHostService {
    private static final String CACHE_PREFIX_KEY = "websocket:keys:";
    private static final String CACHE_HOSTS_KEY = "websocket:keys:bindHosts";
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void registWsHost(String userCode, String host) {
        String cacheKey = getCacheKey(userCode);

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(cacheKey,host);
        redisTemplate.expire(cacheKey,24, TimeUnit.HOURS);
    }
    @Override
    public void bindHost(String host){
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(CACHE_HOSTS_KEY,host);
        redisTemplate.expire(CACHE_HOSTS_KEY,365, TimeUnit.DAYS);
    }
    public void unBindHost(String host){
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(CACHE_HOSTS_KEY,host);
    }

    @Override
    public void unRegistHost(String userCode, String host) {
        String cacheKey = getCacheKey(userCode);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(cacheKey,host);
    }

    @Override
    public List<String> getRegistHosts(String userCode) {
        String cacheKey = getCacheKey(userCode);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members(cacheKey);

        return Optional.ofNullable(members).map(datas -> {
            List<String> list = new ArrayList<>();
            list.addAll(datas);
            return list;
        }).orElse(Collections.EMPTY_LIST);
    }

    @Override
    public List<String> getRegistHostsWithoutLocal(String userCode,String host) {
        List<String> allHosts = getRegistHosts(userCode);

        return allHosts.stream().filter(h -> !h.equals(host)).collect(Collectors.toList());
    }
    public List<String> getBindHosts(){
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members(CACHE_HOSTS_KEY);
        return Optional.ofNullable(members).map(datas -> {
            List<String> list = new ArrayList<>();
            list.addAll(datas);
            return list;
        }).orElse(Collections.EMPTY_LIST);
    }

    private String getCacheKey(String userCode){
        return CACHE_PREFIX_KEY + Consts.STR_SPLIT + userCode;
    }
}
