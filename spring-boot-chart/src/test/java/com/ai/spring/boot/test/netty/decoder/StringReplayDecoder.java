package com.ai.spring.boot.test.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 字符串的分包解码器DEMO(ReplayingDecoder)
 * 1. 在协议的Head部分放置字符串的字节长度。Head部分可以用 一个整型int来描述即可
 * 2. 在协议的Content部分，放置的则是字符串的字节数组
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class StringReplayDecoder extends ReplayingDecoder<StringReplayDecoder.Status> {
    enum Status {
        PARSE_1, PARSE_2
    }

    private int length;
    private byte[] inBytes;

    public StringReplayDecoder() {
        //构造函数中，需要初始化父类的state 属性，表示当前阶段
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 分两个阶段解码
        // 1. Head部分
        // 2. Content部分
        switch (state()) {
            case PARSE_1:
                //第一步，从装饰器ByteBuf 中读取长度
                length = in.readInt();
                inBytes = new byte[length];
                // 进入第二步，读取内容
                // 并且设置“读指针断点”为当前的读取位置
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                //第二步，从装饰器ByteBuf 中读取内容数组
                in.readBytes(inBytes, 0, length);
                out.add(new String(inBytes, "UTF-8"));
                // 第二步解析成功，
                // 进入第一步，读取下一个字符串的长度
                // 并且设置“读指针断点”为当前的读取位置
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }

}
