package com.ai.spring.boot.test.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ReplayingDecoder类是ByteToMessageDecoder的子类。
 * 1. 在读取ByteBuf缓冲区的数据之前，需要检查缓冲区是否有足够的 字节。
 * 2. 若ByteBuf中有足够的字节，则会正常读取；反之，如果没有足够 的字节，则会停止解码。
 * 使用ReplayingDecoder基类，编写整数解码器，则可以不用进行长 度检测。
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class Byte2IntegerReplayDecoder extends ReplayingDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int i = in.readInt();
        log.info("解码出一个整数: " + i);
        out.add(i);
    }
}
