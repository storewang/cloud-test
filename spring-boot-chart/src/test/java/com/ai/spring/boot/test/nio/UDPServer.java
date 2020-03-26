package com.ai.spring.boot.test.nio;

import com.ai.spring.boot.test.util.Print;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * UDPServer测试
 * UDP协议不是面向连接的协议.使用UDP协议时，只要知道服务器的IP和端口，就可以直接向对方 发送数据
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
public class UDPServer {
    public static void main(String[] args) throws IOException {
        new UDPServer().receive();
    }
    public void receive() throws IOException {
        // 1. 获取DatagramChannel数据报通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
        datagramChannel.bind(new InetSocketAddress("127.0.0.1", 18899));
        Print.tcfo("UDP 服务器启动成功！");

        // 2. 获取选择器实例
        Selector selector = Selector.open();
        // 3. 注册datagramChannel到选择器上,监控可读事件
        datagramChannel.register(selector, SelectionKey.OP_READ);
        // 4. 轮询选择器查看已经注册的事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isReadable()){
                    // 5. 读取通道数据
                    SocketAddress client = datagramChannel.receive(buffer);
                    // 反转，设置为读模式
                    buffer.flip();
                    Print.tcfo(new String(buffer.array(), 0, buffer.limit()));
                    // 清空buffer,让buffer进行写模式.
                    buffer.clear();
                }
            }
            iterator.remove();
        }
        selector.close();
        datagramChannel.close();
    }
}
