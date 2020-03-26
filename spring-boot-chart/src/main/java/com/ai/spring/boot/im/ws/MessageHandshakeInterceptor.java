package com.ai.spring.boot.im.ws;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * ws握手拦截器，用于鉴权
 *
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
//@Component
public class MessageHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * 握手成功前，可在此进行用户鉴权处理
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param attributes     这里可以存放鉴权成功或是失败的数据，用于向下传播(afterConnectionEstablished这个方法中可以获取)
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, @Nullable Exception e) {

    }
}
