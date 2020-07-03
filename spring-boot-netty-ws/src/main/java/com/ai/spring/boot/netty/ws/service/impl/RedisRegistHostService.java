package com.ai.spring.boot.netty.ws.service.impl;

import com.ai.spring.boot.netty.ws.service.RegistHostService;
import com.ai.spring.boot.netty.ws.util.Consts;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RedisRegistHostService implements RegistHostService {
    private static final String CACHE_PREFIX_KEY = "websocket:keys:";
    private static final String CACHE_HOSTS_KEY  = "websocket:keys:bindHosts";
    private static final String HOST_ONLINES_KEY = "websocket:keys:host:onlines:";
    private static final String TOTAL_ONLINES_KEY= "websocket:keys:total:onlines";
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void registWsHost(String userCode, String host) {
        String cacheKey = getCacheKey(userCode);

        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(cacheKey,host);
        redisTemplate.expire(cacheKey,24, TimeUnit.HOURS);

        String hostNumsKey = HOST_ONLINES_KEY + host;
        redisTemplate.opsForValue().increment(hostNumsKey);
        redisTemplate.opsForValue().increment(TOTAL_ONLINES_KEY);
    }
    @Override
    public void bindHost(String host){
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(CACHE_HOSTS_KEY,host);
        redisTemplate.expire(CACHE_HOSTS_KEY,365, TimeUnit.DAYS);
    }
    @Override
    public void unBindHost(String host){
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Long remove = setOperations.remove(CACHE_HOSTS_KEY, host);

        log.info("-------删除Key={},value={},result:{}----------",CACHE_HOSTS_KEY,host,remove);
    }

    @Override
    public void unRegistHost(String userCode, String host) {
        String cacheKey = getCacheKey(userCode);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(cacheKey,host);

        String hostNumsKey = HOST_ONLINES_KEY + host;
        redisTemplate.opsForValue().decrement(hostNumsKey);
        redisTemplate.opsForValue().decrement(TOTAL_ONLINES_KEY);
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
    @Override
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
