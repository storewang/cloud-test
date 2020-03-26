package com.ai.spring.boot.test.netty.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * MessageToMessageEncoder DEMO
 *
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
@Slf4j
public class String2IntegerEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext c, String s, List<Object> list) throws Exception {
        char[] array = s.toCharArray();
        for (char a : array) {
            //48 是0的编码，57 是9 的编码
            if (a >= 48 && a <= 57) {
                list.add(new Integer(a));
            }
        }
    }
}
