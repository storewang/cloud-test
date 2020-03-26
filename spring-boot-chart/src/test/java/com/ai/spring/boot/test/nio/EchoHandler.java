package com.ai.spring.boot.test.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * IO事件的输入输出处理
 *
 * @author 石头
 * @Date 2020/3/4
 * @Version 1.0
 **/
@Slf4j
public class EchoHandler implements Runnable{
    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                     // 接收状态
    static final int RECIEVING = 0,
                     // 发送状
                     SENDING = 1;
    int state = RECIEVING;

    public EchoHandler(Selector selector, SocketChannel channel) throws IOException {
        this.channel = channel;
        channel.configureBlocking(false);
        // 仅仅取得选择键，后面再设置感兴趣的IO事件
        sk = channel.register(selector,0);

        // 将Handler作为选择键的附件
        sk.attach(this);
        // 注册Read就绪事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == SENDING){
                // 写入通道
                channel.write(byteBuffer);
                // 写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                // 写完后,注册read就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                // 写完后,进入接收的状态
                state = RECIEVING;
            }else if (state == RECIEVING){
                // 从通道读
                int length = 0;
                while ((length = channel.read(byteBuffer)) > 0) {
                    log.info(new String(byteBuffer.array(), 0, length));
                }
                // 读完后，准备开始写入通道,byteBuffer切换成读模式
                byteBuffer.flip();
                // 读完后，注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                // 读完后,进入发送的状态
                state = SENDING;
            }
            // 处理结束了, 这里不能关闭select key，需要重复使用
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
