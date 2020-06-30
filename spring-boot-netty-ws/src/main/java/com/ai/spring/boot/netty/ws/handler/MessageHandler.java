package com.ai.spring.boot.netty.ws.handler;

import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;

/**
 * 消息处理
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
public interface MessageHandler {
    void handler(DispatchMsgRequest request);
}
