package com.ai.spring.boot.test.netty.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 出站pipeLine Demo
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class OutPipelineDemo {
    public class SimpleOutHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.info("出站处理器 A: 被回调" );
            super.write(ctx, msg, promise);
        }
    }
    public class SimpleOutHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.info("出站处理器 B: 被回调" );
            super.write(ctx, msg, promise);
        }
    }
    public class SimpleOutHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.info("出站处理器 C: 被回调" );
            super.write(ctx, msg, promise);
        }
    }

    @Test
    public void testPipelineOutBound() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new SimpleOutHandlerA());
                ch.pipeline().addLast(new SimpleOutHandlerB());
                ch.pipeline().addLast(new SimpleOutHandlerC());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //向通道写一个出站报文
        channel.writeOutbound(buf);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class SimpleOutHandlerB2 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            log.info("出站处理器 B: 被回调" );
            //不调用基类的channelRead, 终止流水线的执行
//            super.write(ctx, msg, promise);
        }
    }

    @Test
    public void testPipelineOutBoundCutting() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new SimpleOutHandlerA());
                ch.pipeline().addLast(new SimpleOutHandlerB2());
                ch.pipeline().addLast(new SimpleOutHandlerC());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //向通道写一个出站报文
        channel.writeOutbound(buf);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
