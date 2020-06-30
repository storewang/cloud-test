package com.ai.spring.boot.flux.service;

import com.ai.spring.boot.flux.ws.service.RegistHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis 操作服务类
 *
 * @author 石头
 * @Date 2020/6/1
 * @Version 1.0
 **/
@Service
public class RedisService implements RegistHostService {
    private static final String CACHE_PREFIX_KEY = "websocket:keys:";
    private static final String CACHE_VAL_LINK   = ":";
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private ServerProperties serverProperties;
    @Override
    public void registWsHost(String uid,String sessionId){
        // websocket:keys:uid:sessionId下面保存的是当前的host:port
        // websocket:keys:uid下保存的sessionId的列表缓存key
        // 如果应用重启应该删除这个机器下的所有缓存信息，让客户重新注册
        // websocket:keys:host:port 保存uid
        // 下面这些操作应该放入Lua脚本中进行统一处理
        String cacheKey = getCacheKey(uid,sessionId);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(cacheKey,getLocalHost());
        String cacheSessionKeys = CACHE_PREFIX_KEY + uid;
        setOperations.add(cacheSessionKeys,sessionId);
        String uidCacheKeys = CACHE_PREFIX_KEY + getLocalHost();
        setOperations.add(uidCacheKeys,uid);

    }

    public Boolean isUserOnline(String uid){
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String cacheSessionKeys = CACHE_PREFIX_KEY + uid;
        Long size = setOperations.size(cacheSessionKeys);
        return Optional.ofNullable(size).filter(key -> key!=null && key.compareTo(0L)>0).map(key -> Boolean.TRUE).orElse(Boolean.FALSE);
    }
    @Override
    public void unRegistHost(String uid,String sessionId){
        String cacheKey = getCacheKey(uid,sessionId);
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(cacheKey,getLocalHost());
        String cacheSessionKeys = CACHE_PREFIX_KEY + uid;
        setOperations.remove(cacheSessionKeys,sessionId);
        // 这里不进行websocket:keys:host:port这个key的删除，当应用重启时进行清除操作。
    }
    public void clearHostBindUsers(){
        String uidCacheKeys = CACHE_PREFIX_KEY + getLocalHost();
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members(uidCacheKeys);

        if (!CollectionUtils.isEmpty(members)){
            members.stream().forEach(uid -> {
                String cacheSessionKeys = CACHE_PREFIX_KEY + uid;
                Set<String> userSessions = setOperations.members(cacheSessionKeys);

                userSessions.stream().forEach(sessionId -> unRegistHost(uid,sessionId));
            });

            setOperations.remove(uidCacheKeys, members.toArray(new String[members.size()]));
        }
    }
    @Override
    public List<String> getRegistHosts(String uid){
        String cacheKey = CACHE_PREFIX_KEY + uid;
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        Set<String> members = setOperations.members(cacheKey);
        List<String> registHosts = new ArrayList<>();
        members.stream().forEach(cacheSessionKey -> {
            String key = getCacheKey(uid,cacheSessionKey);
            Set<String> hosts = setOperations.members(key);
            if (!CollectionUtils.isEmpty(hosts)){
                registHosts.addAll(hosts);
            }
        });


        return registHosts;
    }
    @Override
    public List<String> getRegistHostsWithoutLocal(String uid){
        List<String> allHosts = getRegistHosts(uid);

        return allHosts.stream().filter(host -> !host.equals(getLocalHost())).collect(Collectors.toList());
    }

    private String getCacheKey(String uid,String sessionId){
        return CACHE_PREFIX_KEY + uid + CACHE_VAL_LINK + sessionId;
    }
    private String getLocalHost(){
        String localHostAddress = getLocalHostAddress();
        return getRegistVal(localHostAddress,serverProperties.getPort());
    }
    private String getLocalHostAddress(){
        String hostAddress = "127.0.0.1";
        InetAddress address = serverProperties.getAddress();
        if (address == null){
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                hostAddress = localHost.getHostAddress();
            } catch (UnknownHostException e) {
                hostAddress = "127.0.0.1";
            }
        }else {
            hostAddress = address.getHostAddress();
        }
        return hostAddress;
    }
    private String getRegistVal(String host,Integer port){
        return host + CACHE_VAL_LINK + port;
    }
}
