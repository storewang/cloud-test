package com.ai.spring.boot.test.netty.decoder;

import com.ai.spring.boot.test.util.RandomUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Byte Integer Decoder Tester
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class NettyDecoderTester {
    /**
     * 整数to 字符串解码器的使用实例
     */
    @Test
    public void testIntegerToStringDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerDecoder());
                ch.pipeline().addLast(new Integer2StringDecoder());
                ch.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        for (int j = 0; j < 100; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符串解码器的使用实例
     */
    @Test
    public void testStringHeaderDecoder() {
        testStringDecoder(new StringHeaderDecoder());

    }
    /**
     * 字符串解码器的使用实例
     */
    @Test
    public void testStringReplayDecoder() {
        testStringDecoder(new StringReplayDecoder());

    }
    private void testStringDecoder(ByteToMessageDecoder decoder){
        String content = "疯狂创客圈：高性能学习社群!";

        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(decoder);
                ch.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        byte[] bytes = content.getBytes(Charset.forName("utf-8"));
        for (int j = 0; j < 100; j++) {
            //1-3之间的随机数
            int random = RandomUtil.randInMod(3);
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(bytes.length * random);
            for (int k = 0; k < random; k++) {
                buf.writeBytes(bytes);
            }
            channel.writeInbound(buf);
        }
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 整数解码器的使用实例
     */
    @Test
    public void testByte2IntegerReplayDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerReplayDecoder());
                ch.pipeline().addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        for (int j = 0; j < 100; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testByteToIntegerDecoder(){
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerDecoder());
                ch.pipeline().addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        for (int j = 0; j < 100; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
