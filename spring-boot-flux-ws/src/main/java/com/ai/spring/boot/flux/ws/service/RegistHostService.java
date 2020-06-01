package com.ai.spring.boot.flux.ws.service;

import java.util.List;

/**
 * 服务机器注册服务
 *
 * @author 石头
 * @Date 2020/6/1
 * @Version 1.0
 **/
public interface RegistHostService {
    void registWsHost(String uid,String sessionId);
    void unRegistHost(String uid,String sessionId);
    List<String> getRegistHosts(String uid);
    List<String> getRegistHostsWithoutLocal(String uid);
}
