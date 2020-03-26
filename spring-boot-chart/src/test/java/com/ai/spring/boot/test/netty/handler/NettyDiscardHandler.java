package com.ai.spring.boot.test.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息入站处理
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        try{
            log.info("收到消息,丢弃如下:");
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
            }
            System.out.println();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
