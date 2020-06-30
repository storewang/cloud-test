package com.ai.spring.boot.netty.ws.handler;

import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 业务处理基础channel
 *
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
public abstract class UserChannelHandler<T> extends SimpleChannelInboundHandler<T> {
}
