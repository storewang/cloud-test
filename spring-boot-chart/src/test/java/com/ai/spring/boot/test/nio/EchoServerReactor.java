package com.ai.spring.boot.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程Reactor反应器模式，
 * Reactor反应器和Handler处理器，都执行 在同一条线程上
 * 带来了一个问题：当其中某个Handler阻塞 时，会导致其他所有的Handler都得不到执行
 * 如果被 阻塞的Handler不仅仅负责输入和输出处理的业务，还包括负责连接监 听的AcceptorHandler处理器。这个是非常严重的问题
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
public class EchoServerReactor implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverSocket;

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }

    EchoServerReactor() throws IOException {
        // Reactor初始化

        // 1. 获取选择器
        selector = Selector.open();
        // 2. 获取server监听通道
        serverSocket = ServerSocketChannel.open();
        // 3. 绑定监听端口
        serverSocket.socket().bind(new InetSocketAddress("127.0.0.1",9999));
        // 4. 非阻塞
        serverSocket.configureBlocking(false);
        // 5. 将服务监听通道的“接收连接”IO事件注册到选择器上
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        // attach callback object, AcceptorHandler
        // 此方法可以将任何的Java POJO对象，作为附件添加到SelectionKey 实例，相当于附件属性的setter方法。
        // 这方法非常重要，因为在单线程版 本的反应器模式中，需要将Handler处理器实例，作为附件添加到 SelectionKey实例。
        sk.attach(new AcceptorHandler());
    }

    @Override
    public void run() {
        try {
            // 6. 轮询感兴趣的IO就绪事件
            while (!Thread.interrupted()){
                selector.select();
                // 7. 获取选择键集合
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()){
                    // 8. 获取单个选择键
                    SelectionKey selectionKey = it.next();
                    // Reactor负责dispatch收到的事件
                    dispatch(selectionKey);
                }
                selected.clear();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    void dispatch(SelectionKey sk){
        // 调用之前attach绑定到选择键的handler处理器对象
        Runnable handler = (Runnable)sk.attachment();
        if (handler != null) {
            handler.run();
        }
    }

    class AcceptorHandler implements Runnable{

        @Override
        public void run() {
            try{
                SocketChannel channel = serverSocket.accept();
                if (channel!=null){
                    new EchoHandler(selector,channel);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
