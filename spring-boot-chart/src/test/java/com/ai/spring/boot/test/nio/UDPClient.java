package com.ai.spring.boot.test.nio;

import com.ai.spring.boot.test.util.Dateutil;
import com.ai.spring.boot.test.util.Print;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

/**
 * UDPClient 测试
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class UDPClient {
    public static void main(String[] args) throws IOException {
        new UDPClient().send();
    }
    public void send() throws IOException {
        // 1. 获取DatagramChannel数据通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 2. 设置为非阻塞
        datagramChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        Print.tcfo("UDP 客户端启动成功！");
        Print.tcfo("请输入发送内容:");

        // 客户终端输入
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((Dateutil.getNow() + " >>" + next).getBytes());
            // 切换成可读模式
            buffer.flip();

            // 3. 通过DatagramChannel数据报通道发送数据
            datagramChannel.send(buffer,new InetSocketAddress("127.0.0.1", 18899));
            // 清空buffer,切换成可写模式
            buffer.clear();
        }
        // 4. 关闭DatagramChannel数据报通道
        datagramChannel.close();
    }
}
