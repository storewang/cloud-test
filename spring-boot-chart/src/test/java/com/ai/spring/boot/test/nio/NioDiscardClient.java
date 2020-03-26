package com.ai.spring.boot.test.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 客户端的DiscardClient
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class NioDiscardClient {
    public static void main(String[] args) throws IOException {
        startClient();
    }
    public static void startClient() throws IOException {
        // 1. 获取连接通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9999));
        // 2. 切换成非阻塞
        socketChannel.configureBlocking(false);
        //不断的自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {
        }
        log.info("客户端连接成功");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("hello world".getBytes());
        // 切换成读模式
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        socketChannel.shutdownOutput();
        socketChannel.close();
    }
}
