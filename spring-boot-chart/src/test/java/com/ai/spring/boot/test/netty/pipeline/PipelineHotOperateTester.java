package com.ai.spring.boot.test.netty.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Handler业务处理器的热拔插测试
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class PipelineHotOperateTester {
    static class SimpleInHandlerA extends ChannelInboundHandlerAdapter {

        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.info("入站处理器 A: 被回调 ");
            super.channelRead(ctx, msg);
            //从流水线删除当前Handler
            ctx.pipeline().remove(this);
        }

    }

    static class SimpleInHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.info("入站处理器 B: 被回调 ");
            super.channelRead(ctx, msg);
        }
    }

    static class SimpleInHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.info("入站处理器 C: 被回调 ");
            super.channelRead(ctx, msg);
        }
    }

    //测试处理器的热拔插
    @Test
    public void testPipelineHotOperating() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(new SimpleInHandlerB());
                ch.pipeline().addLast(new SimpleInHandlerC());

            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //第一次向通道写入站报文
        channel.writeInbound(buf);

        //第二次向通道写入站报文
        channel.writeInbound(buf);
        //第三次向通道写入站报文
        channel.writeInbound(buf);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
