package com.ai.spring.boot.flux.ws.service;

import com.ai.spring.boot.flux.ws.conf.WebSocketSessionContext;

/**
 * 消息服务
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
public interface MessageService {
    void handlerMsg(WebSocketSessionContext session, String content);
}
