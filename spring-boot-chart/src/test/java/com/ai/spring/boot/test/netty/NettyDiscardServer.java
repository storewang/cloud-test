package com.ai.spring.boot.test.netty;

import com.ai.spring.boot.test.netty.handler.NettyDiscardHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * netty discard Server 测试
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class NettyDiscardServer extends AbstractServer{
    public static void main(String[] args) {
        new NettyDiscardServer(9999).runServer(new NettyDiscardHandler());
    }

    public NettyDiscardServer(int port) {
        super(port);
    }

}
