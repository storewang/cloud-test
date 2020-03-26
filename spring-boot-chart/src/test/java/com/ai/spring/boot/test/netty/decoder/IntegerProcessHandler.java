package com.ai.spring.boot.test.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Byte2IntegerDecoder ProcessHandler
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class IntegerProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer integer = (Integer) msg;
        log.info("打印出一个整数: " + integer);

    }
}
