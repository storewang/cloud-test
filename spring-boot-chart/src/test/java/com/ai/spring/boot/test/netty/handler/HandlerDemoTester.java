package com.ai.spring.boot.test.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * 入站handler DEMO 测试类-EmbeddedChannel
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
public class HandlerDemoTester {
    @Test
    public void testOutHandlerLifeCircle() {
        final OutHandlerDemo handler = new OutHandlerDemo();
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(handler);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);

//        channel.pipeline().addLast(handler);

        //测试出站写入

        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);

        ChannelFuture f = channel.pipeline().writeAndFlush(buf);
        f.addListener((future) -> {
            if (future.isSuccess()) {
                System.out.println("write is finished");
            }
            channel.close();
        });

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testInHandlerLifeCircle() {
        final InHandlerDemo inHandler = new InHandlerDemo();
        //初始化处理器
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler);
            }
        };
        //创建嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        //模拟入站，写一个入站包
        channel.writeInbound(buf);
        channel.flush();
        //模拟入站，再写一个入站包
        channel.writeInbound(buf);
        channel.flush();
        //通道关闭
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
