package com.ai.spring.boot.netty.ws.service;

import java.util.List;

/**
 * 服务机器注册服务
 *
 * @author 石头
 * @Date 2020/7/2
 * @Version 1.0
 **/
public interface RegistHostService {
    void registWsHost(String userCode,String host);
    void unRegistHost(String userCode,String host);
    List<String> getRegistHosts(String userCode);
    List<String> getRegistHostsWithoutLocal(String userCode,String host);
}
