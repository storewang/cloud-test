package com.ai.spring.boot.test.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 仅仅读取客户端通道的输入数据，读 取完成后直接关闭客户端通道；并且读取到的数据直接抛弃掉 （Discard）
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class NioDiscardServer {
    public static void main(String[] args) throws IOException {
        startServer();
    }
    public static void startServer() throws IOException {
        // 1. 获取选择器
        Selector selector = Selector.open();
        // 2. 获取server监听通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 3. 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 4. 绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(9999));
        log.info("服务器启动成功");
        // 5. 将服务监听通道的“接收连接”IO事件注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 6. 轮询感兴趣的IO就绪事件
        while (selector.select() > 0){
            // 7. 获取选择键集合
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()){
                // 8. 获取单个选择键,并处理
                SelectionKey selectionKey = iterator.next();
                // 9. 判断 key是具体什么事件
                if (selectionKey.isAcceptable()){
                    // 10. 若选择键的IO事件是"连接就绪"，就获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 11. 切换为非阻塞
                    socketChannel.configureBlocking(false);
                    // 12. 将该新连接的通道可读事件注册到选择器上
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else if (selectionKey.isReadable()){
                    // 13. 若选择键的IO事件是“可读”事件，读取数据
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    // 14. 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    while ((length = socketChannel.read(buffer))>0){
                        // 切换为读模式
                        buffer.flip();
                        log.info(new String(buffer.array(),0,length));
                        // 清空，并切换到写模式
                        buffer.clear();
                    }

                    socketChannel.close();
                }
            }
            iterator.remove();
        }
        serverSocketChannel.close();
    }
}
