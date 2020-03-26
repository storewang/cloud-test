package com.ai.spring.boot.test.netty;

import com.ai.spring.boot.test.netty.handler.NettyEchoServerHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Netty Echo Server
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class NettyEchoServer extends AbstractServer{
    public static void main(String[] args) {
        new NettyDiscardServer(9999).runServer(NettyEchoServerHandler.INSTANCE);
    }

    public NettyEchoServer(int port) {
        super(port);
    }
}
