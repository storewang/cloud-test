package com.ai.spring.boot.test.netty.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * MessageToMessageDecoder DEMO
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
public class Integer2StringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
