package com.ai.spring.boot.test.nio;

import com.ai.spring.boot.test.util.Dateutil;
import com.ai.spring.boot.test.util.Print;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 单线程Reactor反应器测试客户端
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class EchoClient {
    public static void main(String[] args) throws IOException {
        new EchoClient().start();
    }
    public void start() throws IOException {
        // 1. 获取连接通道（channel）
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9999));
        // 2. 切换成非阻塞
        socketChannel.configureBlocking(false);
        //不断的自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {
        }
        Print.tcfo("客户端启动成功！");
        // 3. 启动接受线程
        Processer processer = new Processer(socketChannel);
        new Thread(processer).start();
    }
    class Processer implements Runnable{
        final Selector selector;
        final SocketChannel channel;
        Processer(SocketChannel channel) throws IOException {
            // 4. Reactor初始化
            // 1). 获取选择器
            selector = Selector.open();
            this.channel = channel;
            // 2). 将连接通道的“读写”IO事件注册到选择器上
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }
        @Override
        public void run() {
            try {
                // 5. 轮询感兴趣的IO就绪事件
                while (!Thread.interrupted()){
                    selector.select();
                    Set<SelectionKey> selected = selector.selectedKeys();
                    Iterator<SelectionKey> it = selected.iterator();
                    while (it.hasNext()){
                        SelectionKey sk = it.next();

                        if (sk.isWritable()){
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            // 等待终端输入
                            Scanner sanner = new Scanner(System.in);
                            Print.tcfo("请输入发送内容:");
                            if (sanner.hasNext()){
                                // 获取输入连接通道,若选择键的IO事件是“可写”事件
                                SocketChannel socketChannel = (SocketChannel) sk.channel();
                                String next = sanner.next();
                                buffer.put((Dateutil.getNow() + ">>" + next).getBytes());
                                // 写入完成，切换成读模式
                                buffer.flip();

                                // 发送数据
                                socketChannel.write(buffer);
                                // 发送完成，buff切换成写模式
                                buffer.clear();
                            }
                        }
                        if (sk.isReadable()){
                            // 若选择键的IO事件是“可读”事件,读取数据
                            SocketChannel socketChannel = (SocketChannel) sk.channel();
                            // 读取数据
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int length = 0;
                            while ((length = socketChannel.read(byteBuffer)) > 0) {
                                byteBuffer.flip();
                                log.info("server echo:" + new String(byteBuffer.array(), 0, length));
                                byteBuffer.clear();
                            }
                        }
                        // 处理结束了, 这里不能关闭select key，需要重复使用
                    }
                    selected.clear();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
