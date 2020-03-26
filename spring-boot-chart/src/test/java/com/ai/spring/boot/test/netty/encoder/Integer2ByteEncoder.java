package com.ai.spring.boot.test.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Integer Byte Encoder
 * 1. MessageToByteEncoder是一个非常重要的编码器基类,MessageToByteEncoder的功能是将一个 Java POJO对象编码成一个ByteBuf数据包。
 * 2. 编码器是ChannelOutboundHandler出站处理器的实现类。
 * 3. 由于最后只有ByteBuf才能写入到通道中去，因此可以肯定通道流 水线上装配的第一个编码器一定是把数据编码成了ByteBuf类型。
 *    这里 有个问题：为什么编码成的ByteBuf类型数据包的编码器是在流水线的 头部，而不是在流水线的尾部呢？
 *    原因很简单：因为出站处理的顺序是 从后向前的。
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class Integer2ByteEncoder extends MessageToByteEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
        log.info("encoder Integer = " + msg);
    }
}
