package com.ai.spring.boot.test.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Byte Integer Decoder
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class Byte2IntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= 4) {
            int i = in.readInt();
            log.info("解码出一个整数: " + i);
            out.add(i);
        }
    }
}
